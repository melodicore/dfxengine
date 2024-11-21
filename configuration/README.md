DFXEngine Configuration contains cascading configurations with arbitrary types.

## [`ConfigurationManager`](src/main/java/me/datafox/dfxengine/configuration/ConfigurationManagerImpl.java)

`ConfigurationManager` is a singleton class that holds a global `Configuration` instance. The implementation is
annotated as a `@Component` for the [Injector](../injector) module and will be automatically instantiated by it.

## [`Configuration`](src/main/java/me/datafox/dfxengine/configuration/ConfigurationImpl.java)

`Configuration` is a configuration object that holds values of arbitrary types associated with keys. `ConfigurationKey` 
is used to determine the type and default value of a configuration entry, and values to configuration entries may be 
static objects or a `Supplier<T>`. Configuration keys should be public static final fields.

Configurations are designed to be copied and applied on top of other configurations in a cascading manner.

Configurations can be registered as a `@Component` using method instantiation. Normal configurations will be applied to
the configuration manager using `@Initialize`, at which point all components have been instantiated. If some 
configuration is required at instantiation time, 
[`InitialConfiguration`](src/main/java/me/datafox/dfxengine/configuration/InitialConfiguration.java) can be used as a 
component to achieve that.