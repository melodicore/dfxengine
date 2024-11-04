DFXEngine Injector is a dependency injector. It scans the classpath for all classes, fields and methods with specific
annotations and instantiates/invokes them automatically, resolving and injecting dependencies as it does. From version
2.0.0 onwards type parameters are supported as well.

## Important

Before building the injector with `InjectorBuilder.build()`, you must first call one of the scan methods. The 
`InjectorBuilder.scan()` method is recommended for most use cases, but if you need specific scan parameters or if your 
environment (Android, GraalVM) requires build time scanning, you can use `InjectorBuilder.load(ClassHierarchy)`. 
[`ClassHierarchy`](src/main/java/me/datafox/dfxengine/injector/serialization/ClassHierarchy.java) is a class 
serializable by any serializer that supports serializing `Class<T>` objects, and the scanner makes sure to only include
classes that can be retrieved with `Class.forName(String)`. You can generate it with the `scan()` methods in 
[`ClassScanner`](src/main/java/me/datafox/dfxengine/injector/ClassScanner.java).

## Dependencies

Any class that is instantiated by the injector can be used as a dependency, and there are three different cases where
this would happen.

1. If a class is annotated with `@Component`, it is a valid dependency.
2. If a class contains a non-static method annotated with `@Component`, it is a valid dependency.
3. If a class is the declared return type of a method annotated with `@Component`, it is a valid dependency.

`@Inject` annotations on constructors and fields, and `@Initialize` annotations on methods are recognised in cases 1 
and 2.

When resolving dependencies, any component that's type is assignable to the dependency's declared type is considered
a valid dependency. This includes all type parameters. So for example, declaring a dependency as 
`Supplier<CharSequence>` will return all components that implement Supplier, with any type parameter that implements
`CharSequence`. Using wildcards is allowed, but internally `?` resolves to `Object`, and 
<code>?&nbsp;extends&nbsp;Type</code> resolves to `Type`. <code>?&nbsp;super&nbsp;Type</code> works as intended since 
version 2.1.0.

### Special dependencies

* `List` (`java.util.List`) determines that multiple components of the same type are requested. The actual type of 
component requested will be the type parameter of the list. The dependency must declare List specifically, any extending 
interfaces or implementing classes will be treated as regular dependencies.
* [`InstantiationDetails`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/InstantiationDetails.java) is 
automatically instantiated for every component that depends on it, and contains a reference to the class of the 
component and its type parameters. For method components, the class and type parameters are the method's declared type, 
not the type of the object actually returned by the method. If the component's instantiation policy is `PER_INSTANCE`, 
details will also contain a reference to the class and type parameters of the component that has the first component as 
a dependency.

### Preset dependencies

The injector module currently declares three preset components to be used as dependencies.

* [`Injector`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/Injector.java) can be used to request any 
arbitrary component or components during runtime by using the `getComponent(...)` and `getComponents(...)` methods.
* `Logger` (`org.slf4j.Logger`) can be used to request a slf4j logger. It has the `PER_INSTANCE` instantiation policy 
and uses the requesting component's signature from `InstantiationDetails` as a parameter to `LoggerFactory.getLogger()` 
internally to get the appropriate logger, the method of which can be selected with `LoggerType`.
* [`LoggerType`](src/main/java/me/datafox/dfxengine/injector/InjectorImpl.java) is an enum with three possible values 
that determine how the `Logger` name is resolved.
  * `CLASSIC` is the normal way of using the requesting component's `Class` as a parameter for
  `LoggerFactory.getLogger()`.
  * `PARAMETERS` includes type parameters in the logger name, but stripping their package prefixes for more concise 
  output. This is the default option if no `LoggerType` component is declared by the user.
  * `FULL_PARAMETERS` is the same as `PARAMETERS` but does not strip the package prefixes.

## Events

Events were added in 2.1.0 and allow runtime invocation of specific methods. Any methods annotated with `@EventHandler` 
are registered as event methods. Event methods must have exactly one parameter. When calling 
`Injector.invokeEvent(Object)`, all Components that have `InstantiationPolicy.ONCE` and contain event handler methods, 
superclass and interface methods included, will have those methods invoked with the given object.

If the event handler method returns an object, that object will be treated as an event as well. This allows for chaining
events. If `null` is returned, no event is invoked. Events that are invoked this way are handled breadth first.

## Annotations

### [`@Component`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Component.java)

The `@Component` annotation can be used for both classes and methods. 

A component class will be instantiated with a specific constructor (see [`@Inject`](#inject)), and a component method 
will be invoked. In both cases, all parameters of the constructor/method will be treated as dependencies. If a component 
method is not static, its declaring class will be treated as if it was also annotated as a component and will be subject 
to all the same rules.

If a method annotated as component is `void`, the method will be treated as a special void component. Void components
are invoked once after everything else is invoked, including methods annotated with `@Initialize`. This is useful when 
you need to validate other components in ways that are not covered by the Injector itself.

If a component method's declared return type is `List`, the list's type parameter will be used as the component's type 
instead and the method will effectively return multiple components. The method must declare List specifically, any 
extending interfaces or implementing classes will be treated as regular dependencies.

The `@Component` annotation has two parameters. 
* The value parameter determines the 
[`InstantiationPolicy`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/InstantiationPolicy.java). The
default value is `ONCE`, which means that the component will be instantiated at build time and the instance will be used
for all components that depend on it. If the value is set to `PER_INSTANCE`, the component will instead be instantiated
for every component that depends on it. `PER_INSTANCE` has no effect on void components, and a warning will be logged if
void components with `PER_INSTANCE` exist.
* The second parameter is `order`, an integer normally set to `0`. It determines the order of components in a list when 
multiple components are requested. It also prioritises components when a single component is requested and multiple 
components are present, where lower value means higher priority. An exception is only thrown when a single component is 
requested but multiple components have the same lowest value for `order`. On void components, `order` determines the
invocation order.

### [`@Inject`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Inject.java)

The `@Inject` annotation can be used for both constructors and fields.

Any class that is instantiated by the injector (by having the `@Component` annotation either on the class itself or on
any declared method) must have a valid constructor. A valid constructor is any constructor that is annotated with 
`@Inject` and only contains parameters that are valid dependencies. There may be only one injected constructor, and an 
exception is thrown if multiple are present. If none are present, the presence of a default constructor is checked for 
instead to be invoked, and if that is not present either an exception is thrown.

Any non-final field annotated with `@Inject` on a class annotated with, containing non-static methods annotated with 
`@Component` or a class that is returned by a component method will be treated as a dependency, and will be injected 
right after the component in question is instantiated. This includes the declaring classes of non-static component 
methods. If an injectable field is present that is final, an exception is thrown.

`@Inject` annotations on classes that are not components or contain non-static component methods are ignored.

### [`@Initialize`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Initialize.java)

The `@Initialize` annotation can be used for methods. All initializer methods declared on classes annotated with or
containing non-static methods annotated with `@Component` will be invoked and their parameters will be treated as
dependencies and injected, but only after all components have been instantiated. This is especially useful when avoiding 
cyclic dependencies would be problematic, as the parameters can be any components, even the ones that depend on the 
component declaring the method.

The `@Initialize` annotation has one parameter, `value`, which is an integer that determines the invocation order of the 
methods. A lower value means the method is invoked sooner, and the default value is `0`.

Initializer methods on components with the `PER_INSTANCE` instantiation policy will be invoked for every request of that
component, even if the methods are static. Initializer methods on classes that are not components or otherwise 
instantiated by the Injector are ignored.

### [`@EventHandler`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/EventHandler.java)

The `@EventHandler` annotation can be used for methods. The behavior of event handler methods is described above in the
[Events](#events) section.

## Third party libraries

The injector module uses [ClassGraph](https://github.com/classgraph/classgraph) internally to scan the classpath.