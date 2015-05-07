Not RxAndroid
=============

A from-scratch, re-implementation of RxAndroid with a consistent, opinionated API.

This library will not have releases and is subject to deletion at any time.



Ideology
--------

Weak references should not be used. RxJava's subscription graph allows for proper garbage
collections of reference-holding objects provided the caller unsubscribes.

Mapping an observable to an Android event (e.g., view clicks) is a direct mapping. The
library is not responsible for supporting multiple observables bound to the same view.
Multiple listeners to the same view events can be achieved through `share()`.

Naming conventions of classes and their packages should provide unambiguous information
on where functionality can be found. Helpers for platform classes can be found in packages
of the same name but prefixed with `rx.` and classes of the same name but prefixed with `Rx`.
For example, `android.widget.TextView` helpers are in `rx.android.widget.RxTextView`.

Observable factory method names is the plural of the verb (e.g., click --> `clicks()`). The verb
should be in the present tense, regardless of the platform's use (e.g., selected -> selection).
When there are multiple versions of the same verb, prefix with a qualifying noun or adjective that
differentiates (e.g., click vs. long click, item selection vs. nothing selection).

Each observable method factory also has an overload named in the singular and suffixed with
"Events". This overload emits wrapper objects containing additional information about the event
(origin view, timestamp). The name of the wrapper object is the concatenation of the view simple
name, the verb (with optional adverb prefix), and "Event". These classes are in the public API.

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



License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
