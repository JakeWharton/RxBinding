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

Naming conventions of methods: TODO



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
