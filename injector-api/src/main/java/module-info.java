/**
 * Injector is a module containing a dependency injector and an event handling system. This module contains all of its
 * annotations and interfaces and some simple classes.
 *
 * @author datafox
 */
open module dfxengine.injector.api {
    requires static lombok;

    exports me.datafox.dfxengine.injector.api;
    exports me.datafox.dfxengine.injector.api.annotation;
    exports me.datafox.dfxengine.injector.api.exception;
}