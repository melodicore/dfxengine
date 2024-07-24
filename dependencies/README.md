DFXEngine Dependencies is a simple library for handling arbitrary dependency graphs and invalidation. It consists of two 
interfaces and two abstract implementations.

## [`Dependency`](src/main/java/me/datafox/dfxengine/dependencies/Dependency.java)

`Dependency` is an interface that is to be implemented by any class that other classes depend on. It has one