DFXEngine is a collection of Java libraries meant primarily for small-to-medium game 
development, but with features other kinds of developers might also appreciate. It
consists of multiple modules, each with a single purpose. Despite its name, it is not
a game engine, but it does provide many features that a game engine would also provide.

The project is currently in very early development. Most modules are under initial
development or not started or even thought of at all. I build this project primarily
for my own usage, but decided to share the code in case others might find it useful
as well. Maven releases will come later when the project is more mature.

The project is written at the JDK 11 language level, but only uses the JDK 8+ features
that are supported by Android's 
[core library desugaring](https://developer.android.com/studio/write/java8-support).

## Modules

Here are the currently existing and planned modules:

| Module                               | Status             | Description                                  |
|--------------------------------------|--------------------|----------------------------------------------|
| [Injector API](injector-api)         | Done (v1.0.0)      | Annotations for the Injector module          |
| [Injector](injector)                 | Done (v1.0.3)      | A dependency injector                        |
| [Handles API](handles-api)           | Done (v1.0.2)      | Interfaces for the Handles module            |
| [Handles](handles)                   | Done (v1.0.0)      | Dynamic enum-like structures for map keys    |
| [Dependencies](dependencies)         | Functional         | Data structure dependencies and invalidation |
| Math                                 | Not yet started    | Number manipulation and presentation         |
| Entities                             | Not yet started    | Data-oriented entity system                  |
| [Utils](utils)                       | Under construction | Utilities used by other modules              |

There are four possible statuses for modules.

 - Done: Production-ready, commented and tested
 - Functional: Feature-complete but subject to minor changes and may lack comments or 
tests
 - Under construction: Incomplete and subject to major changes
 - Not yet started: Planned but without anything concrete

