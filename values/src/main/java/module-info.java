/**
 * @author datafox
 */
module dfxengine.values {
    requires static lombok;

    requires dfxengine.values.api;
    requires dfxengine.math.api;
    requires dfxengine.math;
    requires dfxengine.handles;
    requires dfxengine.utils;

    exports me.datafox.dfxengine.values;
    exports me.datafox.dfxengine.values.modifier;
    exports me.datafox.dfxengine.values.utils;
}