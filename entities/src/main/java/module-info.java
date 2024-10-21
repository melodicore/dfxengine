/**
 * @author datafox
 */
module dfxengine.entities {
    requires static lombok;

    requires dfxengine.injector.api;
    requires dfxengine.entities.api;
    requires dfxengine.handles.api;
    requires dfxengine.handles;
    requires dfxengine.values.api;
    requires dfxengine.math.api;
    requires dfxengine.math;
    requires dfxengine.values;
    requires dfxengine.text.api;
    requires jsonbeans;
    requires dfxengine.dependencies;
    requires dfxengine.utils;
    requires org.slf4j;

    exports me.datafox.dfxengine.entities;
    exports me.datafox.dfxengine.entities.data;
    exports me.datafox.dfxengine.entities.definition;
    exports me.datafox.dfxengine.entities.definition.node;
    exports me.datafox.dfxengine.entities.exception;
    exports me.datafox.dfxengine.entities.node;
    exports me.datafox.dfxengine.entities.utils;
}