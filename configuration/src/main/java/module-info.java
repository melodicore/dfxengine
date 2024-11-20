/**
 * Cascading configurations with arbitrary types.
 *
 * @author datafox
 */
open module dfxengine.configuration {
    requires static lombok;

    requires dfxengine.configuration.api;
    requires dfxengine.injector.api;

    exports me.datafox.dfxengine.configuration;
}