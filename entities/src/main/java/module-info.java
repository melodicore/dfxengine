/**
 * @author datafox
 */
module dfxengine.entities {
    requires static lombok;

    requires org.slf4j;

    requires dfxengine.utils;
    requires dfxengine.injector.api;
    requires dfxengine.entities.api;
    requires dfxengine.handles.api;
    requires dfxengine.handles;
    requires dfxengine.values.api;
    requires dfxengine.math.api;
    requires dfxengine.math;
    requires dfxengine.values;

    exports me.datafox.dfxengine.entities;
    exports me.datafox.dfxengine.entities.utils;
}