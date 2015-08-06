package com.jakewharton.kotlingen

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.ModifierSet
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.type.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.google.common.collect.ImmutableList
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import java.nio.file.Files
import java.nio.file.Path

/**
 * Task that generates Kotlin bindings for the Rx*.java implementations
 *
 * To debug this, you can use the following command and attach a remote debugger to port 5005
 * ./gradlew clean build --no-daemon -Dorg.gradle.debug=true
 */
class KotlinGenTask extends SourceTask {

  /** Regex used for finding references in javadoc links */
  private static final String DOC_LINK_REGEX = "[0-9A-Za-z._]*"

  /**
   * These are imports of classes that Kotlin advises against using and are replaced in
   * {@link #resolveKotlinTypeByName}
   */
  private static final List<String> IGNORED_IMPORTS = ImmutableList.of(
          "java.util.List"
  )

  @TaskAction
  def generate(IncrementalTaskInputs inputs) {
    getSource().each { generateKotlin(it) }
  }

  void generateKotlin(File file) {
    String outputPath = file.parent.replace("java", "kotlin").replace("/src", "-kotlin/src")
    outputPath = outputPath.substring(0, outputPath.indexOf("com/jakewharton"))
    File outputDir = new File(outputPath)
    CompilationUnit cu = JavaParser.parse(file)

    KFile kClass = new KFile()
    kClass.fileName = file.name.replace(".java", ".kt")

    // Visit the appropriate nodes and extract information
    cu.accept(new VoidVisitorAdapter<KFile>() {

      @Override
      void visit(PackageDeclaration n, KFile arg) {
        arg.packageName = n.name
        super.visit(n, arg)
      }

      @Override
      void visit(ClassOrInterfaceDeclaration n, KFile arg) {
        arg.bindingClass = n.name
        arg.extendedClass = n.name.replace("Rx", "")
        super.visit(n, arg)
      }

      @Override
      void visit(MethodDeclaration n, KFile arg) {
        arg.methods.add(new KMethod(n))
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

      @Override
      void visit(ImportDeclaration n, KFile arg) {
        if (!n.static) {
          arg.imports.add(n.name.toString())
        }
        super.visit(n, arg)
      }

    }, kClass)

    kClass.generate(outputDir)
  }

  /**
   * Represents a kotlin file that corresponds to a Java file/class in an RxBinding module
   */
  static class KFile {
    String fileName
    String packageName
    String bindingClass
    String extendedClass
    List<KMethod> methods = []
    List<String> imports = []

    /** Generates the code and writes it to the desired directory */
    public void generate(File directory) {

      StringBuilder builder = new StringBuilder()

      // package
      builder.append("package $packageName\n\n")

      // imports
      imports.each { im ->
        if (!IGNORED_IMPORTS.contains(im)) {
          builder.append("import $im\n")
        }
      }

      // fun!
      methods.each { m -> builder.append("\n${m.generate(bindingClass)}\n") }

      // I'm sure there's a more efficient way to do this...
      String finalCode = builder.toString()

      Path outputDirectory = directory.toPath()
      if (!packageName.isEmpty()) {
        for (String packageComponent : packageName.split("\\.")) {
          outputDirectory = outputDirectory.resolve(packageComponent)
        }
        Files.createDirectories(outputDirectory)
      }

      Path outputPath = outputDirectory.resolve(fileName)
      Writer writer = new OutputStreamWriter(Files.newOutputStream(outputPath))
      try {
        writer.write(finalCode)
      } finally {
        writer.close()
      }
    }
  }

  /**
   * Represents a method implementation that needs to be wired up in Kotlin
   */
  static class KMethod {
    private final String comment
    private final String name
    private final String accessModifier
    private final String extendedClass
    private final List<Parameter> parameters
    private final Type returnType
    private final String typeParameters

    public KMethod(MethodDeclaration n) {
      this.name = n.name
      this.comment = n.comment ? cleanUpDoc(n.comment.toString()) : null
      this.accessModifier = ModifierSet.getAccessSpecifier(n.modifiers).codeRepresenation
      this.extendedClass = n.parameters[0].type.toString()
      this.parameters = n.parameters.subList(1, n.parameters.size())
      this.returnType = n.type
      this.typeParameters = typeParams(n.typeParameters)
    }

    /** Cleans up the generated doc and translates some html to equivalent markdown for Kotlin docs */
    private static String cleanUpDoc(String doc) {
      return doc.replace("<em>", "*")
          .replace("</em>", "*")
          .replace("<p>", "")

          // JavaParser adds a couple spaces to the beginning of these for some reason
          .replace("   *", " *")

          // {@code view} -> `view`
          .replaceAll(/\{@code ($DOC_LINK_REGEX)\}/) { String fullmatch, String codeName -> "`$codeName`" }

          // {@link Foo} -> [Foo]
          .replaceAll(/\{@link ($DOC_LINK_REGEX)\}/) { String fullmatch, String foo -> "[$foo]" }

          // {@link Foo#bar} -> [Foo.bar]
          .replaceAll(/\{@link ($DOC_LINK_REGEX)#($DOC_LINK_REGEX)\}/) { String fullmatch, String foo, String bar -> "[$foo.$bar]" }

          // {@linkplain Foo baz} -> [baz][Foo]
          .replaceAll(/\{@linkplain ($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\}/) { String fullmatch, String foo, String baz -> "[$baz][$foo]" }

          //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
          .replaceAll(/\{@linkplain ($DOC_LINK_REGEX)#($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\}/) { String fullmatch, String foo, String bar, String baz -> "[$baz][$foo.$bar]" }
          .trim()
    }

    /** Generates method level type parameters */
    private static String typeParams(List<TypeParameter> params) {
      if (!params || params.isEmpty()) {
        return null
      }

      StringBuilder builder = new StringBuilder()
      builder.append("<")
      params.each { p -> builder.append("${p.name} : ${p.typeBound[0].name}") }
      builder.append(">")
      return builder.toString()
    }

    /**
     * Generates parameters in a kotlin-style format
     *
     * @param specifyType boolean indicating whether or not to specify the type (i.e. we don't
     *        need the type when we're passing params into the underlying Java implementation)
     */
    private String kParams(boolean specifyType) {
      StringBuilder builder = new StringBuilder()
      parameters.each { p -> builder.append("${p.id.name}${specifyType ? ": " + resolveKotlinType(p.type) : ""}") }
      return builder.toString()
    }

    /**
     * Generates the kotlin code for this method
     *
     * @param bindingClass name of the RxBinding class this is tied to
     */
    public String generate(String bindingClass) {
      ///////////////
      // STRUCTURE //
      ///////////////
      // Javadoc
      // public inline fun DrawerLayout.drawerOpen(): Observable<Boolean> = RxDrawerLayout.drawerOpen(this)
      // <access specifier> inline fun <extendedClass>.<name>(params): <type> = <bindingClass>.name(this, params)

      String fParams = kParams(true)
      String jParams = kParams(false)
      String generated =
              "${comment ? comment : ""}\n" +
                      "$accessModifier inline fun ${typeParameters ? typeParameters + " " : ""}$extendedClass.${name}($fParams): ${resolveKotlinType(returnType)} = $bindingClass.$name(${jParams ? "this, $jParams" : "this"})"

      return generated
    }
  }

  /** Recursive function for resolving a Type into a Kotlin-friendly String representation */
  static String resolveKotlinType(Type inputType) {
    if (inputType instanceof ReferenceType) {
      return resolveKotlinType((inputType as ReferenceType).type)
    } else if (inputType instanceof ClassOrInterfaceType) {
      ClassOrInterfaceType cit = inputType as ClassOrInterfaceType
      String baseType = resolveKotlinTypeByName(cit.name)
      if (!cit.typeArgs || cit.typeArgs.isEmpty()) {
        return baseType
      }
      return "$baseType<${cit.typeArgs.collect {Type type -> resolveKotlinType(type)}.join(", ")}>"
    } else if (inputType instanceof PrimitiveType || inputType instanceof VoidType) {
      return resolveKotlinTypeByName(inputType.toString())
    } else if (inputType instanceof WildcardType) {
      WildcardType wc = inputType as WildcardType
      return "in ${resolveKotlinType(wc.super)}"
    } else {
      throw new NotImplementedException()
    }
  }

  static String resolveKotlinTypeByName(String input) {
    switch (input) {
      case "Object":
        return "Any"
      case "Integer":
        return "Int"
      case "int":
      case "char":
      case "boolean":
      case "long":
      case "float":
      case "short":
      case "byte":
        return input.capitalize()
      case "List":
        return "MutableList"
      default:
        return input
    }
  }
}
