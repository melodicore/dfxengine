/**
 * @author datafox
 */
module dfxengine.text {
    requires static lombok;

    requires dfxengine.dependencies;
    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;

    exports me.datafox.dfxengine.text;
}