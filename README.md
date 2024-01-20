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
| [Collections](collections)   | Done (1.0.0)^1^    | Collections for other modules and general usage     |
| [Dependencies](dependencies) | Done (1.0.2)       | Data structure dependencies and invalidation        |
| [Handles API](handles-api)   | Done (1.0.6)       | Interfaces for the Handles module                   |
| [Handles](handles)           | Done (1.0.6)       | Dynamic enum-like structures for map keys           |
| [Injector API](injector-api) | Done (1.1.2)       | Annotations for the Injector module                 |
| [Injector](injector)         | Done (1.1.2)       | A dependency injector                               |
| [Math API](math-api)         | Done (1.0.2)       | Interfaces for the Math module                      |
| [Math](math)                 | Done (1.0.4)       | Seamless wrapping and manipulation of number types  |
| [Utils](utils)               | Done (1.0.0)^1^    | Utilities used by other modules                     |
| [Values API](values-api)     | Done (1.0.0)       | Interfaces for the Values module                    |
| [Values](values)             | Done (1.0.1)       | Mutable number values and a dynamic modifier system |
| [Text API](text-api)         | Under Construction | Interfaces for the Text module                      |
| [Text](text)                 | Under Construction | Text handling and representation                    |
| Entities                     | Not yet started    | Data-oriented entity system                         |
| Serialization                | Not yet started    | Serialization for other modules                     |

1: These modules will get more features as they are required by other modules

There are four possible statuses for modules.

 - Done: Production-ready, commented and tested
 - Functional: Feature-complete but subject to minor changes and may lack comments or tests
 - Under construction: Incomplete and subject to major changes
 - Not yet started: Planned but without anything concrete

