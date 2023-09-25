/**
 * @author datafox
 */
module dfxengine.values.api {
    requires static lombok;

    requires dfxengine.math.api;
    requires dfxengine.handles.api;
    requires dfxengine.dependencies;

    exports me.datafox.dfxengine.values.api;
    exports me.datafox.dfxengine.values.api.comparison;
    exports me.datafox.dfxengine.values.api.operation;
}