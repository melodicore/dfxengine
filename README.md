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
| [Dependencies](dependencies) | Done (1.0.1)       | Data structure dependencies and invalidation        |
| [Handles API](handles-api)   | Done (1.0.5)       | Interfaces for the Handles module                   |
| [Handles](handles)           | Done (1.0.5)       | Dynamic enum-like structures for map keys           |
| [Injector API](injector-api) | Done (1.1.1)       | Annotations for the Injector module                 |
| [Injector](injector)         | Done (1.1.1)       | A dependency injector                               |
| [Math API](math-api)         | Done (1.0.1)       | Interfaces for the Math module                      |
| [Math](math)                 | Done (1.0.3)       | Seamless wrapping and manipulation of number types  |
| [Utils](utils)               | Under construction | Utilities used by other modules                     |
| [Values API](values-api)     | Under construction | Interfaces for the Values module                    |
| [Values](values)             | Under construction | Mutable number values and a dynamic modifier system |
| Entities                     | Not yet started    | Data-oriented entity system                         |
| Text                         | Not yet started    | Textual presentation for other modules              |
| Serialization                | Not yet started    | Serialization for other modules                     |

There are four possible statuses for modules.

 - Done: Production-ready, commented and tested
 - Functional: Feature-complete but subject to minor changes and may lack comments or 
tests
 - Under construction: Incomplete and subject to major changes
 - Not yet started: Planned but without anything concrete

