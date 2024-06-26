/**
 * @author datafox
 */
open module dfxengine.text.api {
    requires static lombok;

    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.injector.api;
    requires dfxengine.dependencies;

    exports me.datafox.dfxengine.text.api;
}