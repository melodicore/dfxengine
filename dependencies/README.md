DFXEngine Dependencies is a simple library for handling arbitrary dependency graphs and invalidation. It consists of two 
interfaces and two abstract implementations.

## [`Dependent`](src/main/java/me/datafox/dfxengine/dependencies/Dependent.java)

`Dependent` is an interface that is to be implemented by any class that depends on other classes. It has one method, 
`invalidate()`, which is called whenever any of the classes that it depends on has changed. If a dependent is also a
dependency, invalidate must also call invalidate on all other dependents that depend on it.

## [`Dependency`](src/main/java/me/datafox/dfxengine/dependencies/Dependency.java)

`Dependency` is an interface that is to be implemented by any class that other classes depend on. It has a method 
`invalidateDependents()` that should call `invalidate()` on all of its dependents. It also has a variety of methods used
for adding, removing and checking for the presence of dependents. It should not usually be implemented on its own, and
instead the two abstract classes should be extended instead.

## [`AbstractDependency`](src/main/java/me/datafox/dfxengine/dependencies/AbstractDependency.java)

`AbstractDependency` is an abstract implementation of `Dependency` that contains a logger and a set of dependencies.
It provides an implementation for all methods of `Dependency`, but is abstract due to being intended to be extended.
Methods for adding dependents check for cyclic dependencies, but only for dependents that also implement dependency.

## [`DependencyDependent`](src/main/java/me/datafox/dfxengine/dependencies/DependencyDependent.java)

`DependencyDependent` is an abstract extension of `AbstractDependency` that also implements `Dependent`. It provides a
new abstract method, `onInvalidate()` which has the same functionality as `Dependent.invalidate()`, but is separate 
since the class also implements `invalidate()` with code that first calls `onInvalidate()` and then 
`invalidateDependents()`. Overriding `invalidate()` is not recommended because then the user would need to call 
`super.invalidate()` manually, which is the reasoning for the separate `onInvalidate()` method.