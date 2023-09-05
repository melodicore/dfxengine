DFXEngine is a collection of Java libraries meant primarily for small-to-medium game 
development, but with features other kinds of developers might also appreciate. It
consists of multiple modules, each with a single purpose. Despite its name, it is not
a game engine, but it does provide many features that a game engine would also provide.

The project is currently in early development. Some modules are finished, while others
are under development, and more may be introduced. I build this project primarily for 
my own usage, but decided to share the code in case others might find it useful as well.
Maven releases exist in namespace 
[me.datafox.dfxengine](https://central.sonatype.com/namespace/me.datafox.dfxengine)

The project is written at the JDK 11 language level, but only uses the JDK 8+ features
that are supported by Android's 
[core library desugaring](https://developer.android.com/studio/write/java8-support).

## Modules

Here are the currently existing and planned modules:

| Module                       | Status             | Description                                         |
|------------------------------|--------------------|-----------------------------------------------------|
| [Injector API](injector-api) | Done (v1.0.2)      | Annotations for the Injector module                 |
| [Injector](injector)         | Done (v1.0.5)      | A dependency injector                               |
| [Handles API](handles-api)   | Done (v1.0.4)      | Interfaces for the Handles module                   |
| [Handles](handles)           | Done (v1.0.3)      | Dynamic enum-like structures for map keys           |
| [Dependencies](dependencies) | Done (v1.0.0)      | Data structure dependencies and invalidation        |
| [Math API](math-api)         | Under construction | Interfaces for the Math module                      |
| [Math](math)                 | Under construction | Seamless wrapping and manipulation of number types  |
| [Values API](values-api)     | Under construction | Interfaces for the Values module                    |
| [Values](values)             | Under construction | Mutable number values and a dynamic modifier system |
| Entities                     | Not yet started    | Data-oriented entity system                         |
| Text                         | Not yet started    | Textual presentation for other modules              |
| Serialization                | Not yet started    | Serialization for other modules                     |
| [Utils](utils)               | Under construction | Utilities used by other modules                     |

There are four possible statuses for modules.

 - Done: Production-ready, commented and tested
 - Functional: Feature-complete but subject to minor changes and may lack comments or 
tests
 - Under construction: Incomplete and subject to major changes
 - Not yet started: Planned but without anything concrete

