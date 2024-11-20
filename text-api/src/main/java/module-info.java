/**
 * Text generation, manipulation and representation. This module contains interfaces.
 *
 * @author datafox
 */
open module dfxengine.text.api {
    requires static lombok;

    requires dfxengine.configuration.api;
    requires dfxengine.handles.api;

    exports me.datafox.dfxengine.text.api;
}