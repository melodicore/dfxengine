/**
 * Injector is a module containing a dependency injector and an event handling system.
 *
 * @author datafox
 */

module dfxengine.injector {
    requires static lombok;

    requires io.github.classgraph;
    requires org.slf4j;

    requires dfxengine.utils;
    requires dfxengine.injector.api;

    exports me.datafox.dfxengine.injector;
    exports me.datafox.dfxengine.injector.exception;
    exports me.datafox.dfxengine.injector.serialization;
}