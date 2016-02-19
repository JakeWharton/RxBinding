RxBinding
=========

RxJava binding APIs for Android UI widgets from the platform and support libraries.



Download
--------

Platform bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding:0.4.0'
```

'support-v4' library bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding-support-v4:0.4.0'
```

'appcompat-v7' library bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding-appcompat-v7:0.4.0'
```

'design' library bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding-design:0.4.0'
```

'recyclerview-v7' library bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding-recyclerview-v7:0.4.0'
```

'leanback-v17' library bindings:
```groovy
compile 'com.jakewharton.rxbinding:rxbinding-leanback-v17:0.4.0'
```

Kotlin extension methods for all of the above libraries are available by appending `-kotlin` to the
'artifactId' of the dependency. For example, `rxbinding-support-v4` becomes
`rxbinding-support-v4-kotlin`.

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



Development
-----------

The bindings for Android's platform types are in `rxbinding/`.

Weak references should not be used. RxJava's subscription graph allows for proper garbage
collections of reference-holding objects provided the caller unsubscribes.

Mapping an observable to an Android event (e.g., view clicks) is a direct mapping. The
library is not responsible for supporting multiple observables bound to the same view.
Multiple listeners to the same view events can be achieved through operators like `publish()`,
`share()`, or `replay()`. Consult the RxJava documentation for which is appropriate for the
behavior that you want.

Naming conventions of classes and their packages should provide unambiguous information
on where functionality can be found. Helpers for platform classes can be found in packages
of the same name but prefixed with `com.jakewharton.rxbinding.` instead of `android.` and
classes of the same name but prefixed with `Rx`. For example, `android.widget.TextView` bindings
are in `com.jakewharton.rxbinding.widget.RxTextView`.

Observable factory method names is the plural of the verb (e.g., click --> `clicks()`). The verb
should be in the present tense, regardless of the platform's use (e.g., selected -> selection).
When there are multiple versions of the same verb, prefix with a qualifying noun or adjective that
differentiates (e.g., click vs. long click, item selection vs. nothing selection).

If the listener callback provides more than one parameter of useful data, a factory method overload
named in the singular and suffixed with "Events" is included. This overload emits wrapper objects
containing all the additional information about the event. The name of the wrapper object is the
concatenation of the view simple name, the verb (with optional adverb prefix), and "Event". These
classes are in the public API.

Events for listeners with multiple methods should share an abstract base class. The naming follows
the same rules as a normal event class but without the qualifying prefix. An inner `enum` named
"Kind" and associated getter named "kind" should be present on the class. The constructor should
be package-private to prevent subclasses other than those defined for the listener methods. This
class should be in the public API.

The name of the `OnSubscribe` class for each observable is the concatenation of the view simple
name, the verb (with optional prefix), and "OnSubscribe". These classes not in the public API.

Action factory method names are the same as the property (e.g., `enabled`). If the associated
listener has a return value, an overload that accepts a `Func1<E, T>` named "handled" will be
present for determining that value. No error handling will be done. These classes are not in the
public API and are currently defined anonymously.


#### Support modules

Sibling modules exist for various support libraries (such as `rxbinding-recyclerview-v7/` for the
`recyclerview-v7` support library). The folder name and artifact ID is 'rxbinding-' plus the suffix
of the artifact ID of the target library.

The manifest package name for each module is the name of the target library's package but prefixed
with `com.jakewharton.rxbinding` instead of `android.`. For example,
`android.support.v7.recyclerview` becomes `com.jakewharton.rxbinding.support.v7.recyclerview`.
The location of the binding classes and their content follows the same rule as the core 'rxbinding'
module outlined above.


#### Kotlin modules

For the core 'rxbinding' module as well as each support module, a sibling module exists which
provides Kotlin extension methods for each binding method directly on the target type. The name
of each module is the name of the target module with a suffix of '-kotlin'.

These modules do not need to be edited directly. Running the 'generateKotlin' task will create
the bindings automatically from the methods in each respective binding modules. These files should
be checked in when changed.



License
-------

    Copyright (C) 2015 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
