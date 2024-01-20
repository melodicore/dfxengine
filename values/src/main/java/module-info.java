/**
 * @author datafox
 */
module dfxengine.values {
    requires static lombok;

    requires dfxengine.values.api;
    requires dfxengine.math.api;
    requires dfxengine.handles.api;
    requires dfxengine.math;
    requires dfxengine.dependencies;
    requires dfxengine.collections;
    requires dfxengine.utils;

    requires org.slf4j;

    exports me.datafox.dfxengine.values;
    exports me.datafox.dfxengine.values.modifier;
    exports me.datafox.dfxengine.values.operation;
    exports me.datafox.dfxengine.values.utils;
}