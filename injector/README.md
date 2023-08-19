DFXEngine Injector is a simple but powerful dependency injector. It has support for
singleton and multiple instance components, inheritance, lists of dependencies, 
automatic instantiation and arbitrary instantiation of all supported classes. It also
supports components that are instantiated every time they are requested as a dependency.

The Injector has three basic annotations. @Component, @Inject and @Initialize. 

The @Component annotation is used to mark any class or the returned value of any method
as a component for dependency injection. It has one parameter, InstantiationPolicy, 
which determines if it is a singleton or if it is instantiated every time it is 
requested as a dependency. If used on a method, all that method's parameters are treated
as dependencies and injected automatically. If used on a class, dependencies can be
injected with the @Inject annotation. A component class must have a default constructor
or a single constructor annotated with @Inject

The @Inject annotation can be used on a constructor or a field. When used on a 
constructor, all that constructor's parameters are treated as dependencies. When used
on a non-static and non-final field, that field is treated as a dependency and 
automatically initialized after instantiating the class. An

The @Initialize annotation can be used on non-static and non-final methods. All methods
within component classes that are annotated with @Initialize will be invoked
automatically *after all components have been instantiated.* All of their parameters
will be treated as dependencies, and due to this behavior it is most useful when
tackling cyclic dependencies.

The Injector module uses [ClassGraph](https://github.com/classgraph/classgraph) 
internally to resolve all classes in the classpath.