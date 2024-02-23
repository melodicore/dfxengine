DFXEngine Injector is a dependency injector. It scans the classpath for all classes, fields and methods with specific
annotations and instantiates/invokes them automatically, resolving and injecting dependencies as it does. From version
2.0.0 onwards type parameters are supported as well.

## Dependencies

Any class that is instantiated by the injector can be used as a dependency, and there are three different cases where
this would happen.

1. If a class is annotated with `@Component`, it is a valid dependency.
2. If a class contains a non-static method annotated with `@Component`, it is a valid dependency.
3. If a class is the declared return type of a method annotated with `@Component`, it is a valid dependency.

`@Inject` annotations on constructors are recognised in cases 1 and 2, while @Inject annotations on fields and
`@Initialize` annotations on methods are recognised in all three.

When resolving dependencies, any component that's type is assignable to the dependency's declared type is considered
a valid dependency. This includes all type parameters. So for example, declaring a dependency as 
`Supplier<CharSequence>` will return all components that implement Supplier, with any type parameter that implements
`CharSequence`. Using wildcards is technically allowed, but internally `?` and <code>?&nbsp;super&nbsp;Type</code>
resolve to `Object`, and <code>?&nbsp;extends&nbsp;Type</code> resolves to `Type`.

### Special dependencies

* `List` (`java.util.List`) determines that multiple components of the same type are requested. The actual type of 
component requested will be the type parameter of the list. The dependency must declare List specifically, any extending 
interfaces or implementing classes will be treated as regular dependencies.
* [`InstantiationDetails`](src/main/java/me/datafox/dfxengine/injector/InstantiationDetails.java) is automatically 
instantiated for every component that depends on it, and contains a reference to the class of the component and its type
parameters. For method components, the class and type parameters are the method's declared type, not the type of the
object actually returned by the method. If the component's instantiation policy is `PER_INSTANCE`, details will also
contain a reference to the class and type parameters of the component that has the first component as a dependency.

### Preset dependencies

The injector module currently declares two preset components to be used as dependencies.

* [`Injector`](src/main/java/me/datafox/dfxengine/injector/Injector.java) can be used to request any arbitrary component
or components during runtime by using the `getComponent(...)` and `getComponents(...)` methods.
* `Logger` (`org.slf4j.Logger`) can be used to request a slf4j logger. It has the `PER_INSTANCE` instantiation policy 
and uses the requesting class parameter of `InstantiationDetails` as a parameter to `LoggerFactory.getLogger()` 
internally to get the appropriate logger.

### Arrays

Arrays are not allowed as components, and trying to create one with component methods will throw an error, but they are
allowed as parametric types. Internally, references to primitive arrays are stored as is, but references to object 
arrays are not stored as `Object[].class` or equivalent, but instead as `java.lang.reflect.Array.class` with the 
object's class as a parameter. So if you are requesting components manually:

* Do: `Injector.getComponent(Component.class, Parameter.listOf(Array.class, Parameter.of(String.class)))`
* Do not: `Injector.getComponent(Component.class, Parameter.listOf(String[].class))`

## Annotations

### [`@Component`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Component.java)

The `@Component` annotation can be used for both classes and methods. 

A component class will be instantiated with a specific constructor (see `@Inject` below), and a component method will be
invoked. In both cases, all parameters of the constructor/method will be treated as dependencies. If a component method
is not static, its declaring class will be treated as if it was also annotated as a component and will be subject to all
the same rules.

If a component method's declared return type is `List`, the list's type parameter will be used as the component's type 
instead and the method will effectively return multiple components. The method must declare List specifically, any 
extending interfaces or implementing classes will be treated as regular dependencies.

The `@Component` annotation has three parameters. The value parameter determines the 
[`InstantiationPolicy`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/InstantiationPolicy.java). The
default value is `ONCE`, which means that the component will be instantiated at build time and the instance will be used
for all components that depend on it. If the value is set to `PER_INSTANCE`, the component will instead be instantiated
for every component that depends on it. The second parameter is `defaultImpl`, a boolean normally set to `false`. If it 
is set to `true`, the component will be flagged as a default implementation. Whenever a single component is requested as
a dependency but multiple are present, the default implementation components are filtered out and if one remains, that 
will be used as the dependency. The third parameter is `order`, an integer normally set to `0`. It determines the order
of components in a list when multiple components are requested. It also prioritises components when a single component
is requested and multiple non-default components are present, where lower value means higher priority. An exception is 
thrown if multiple components have the same lowest value for `order`.

### [`@Inject`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Inject.java)

The `@Inject` annotation can be used for both constructors and fields.

Any class that is instantiated by the injector (by having the `@Component` annotation either on the class itself or on
any declared method) must have a valid constructor. A valid constructor is any constructor that is annotated with 
`@Inject` and only contains parameters that are valid dependencies. There may be only one injected constructor, and an 
exception is thrown if multiple are present. If none are present, the presence of a default constructor is checked for 
instead to be invoked, and if that is not present either an exception is thrown.

Any non-static field annotated with `@Inject` on a component class will be treated as a dependency, and will be injected 
right after the component in question is instantiated or invoked. This includes the declaring classes of non-static 
component methods, and the class of a component method's declared type, but not the class of the object the method
actually returns. If an injectable field is present that is final, an exception is thrown.

`@Inject` annotations on classes that are not components or otherwise instantiated by the Injector are ignored.

### [`@Initialize`](../injector-api/src/main/java/me/datafox/dfxengine/injector/api/annotation/Initialize.java)

The `@Initialize` annotation can be used for methods. All initializer methods declared on components will be invoked, 
but only after all components have been instantiated, and their parameters will be treated as dependencies and injected. 
This is especially useful when avoiding cyclic dependencies would be problematic, as the parameters can be any 
components, even the ones that depend on the component declaring the method.

The `@Initialize` annotation has one parameter, `value`, which is an integer that determines the invocation order of the 
methods. A lower value means the method is invoked sooner, and the default value is `0`.

Initializer methods on components with the `PER_INSTANCE` instantiation policy will be invoked for every request of that
component, even if the methods are static. Initializer methods on classes that are not components or otherwise 
instantiated by the Injector are ignored.

## Third party libraries

The injector module uses [ClassGraph](https://github.com/classgraph/classgraph) internally to scan the classpath.