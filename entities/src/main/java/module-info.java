/**
 * @author datafox
 */
module dfxengine.entities {
    requires static lombok;

    requires jsonbeans;
    requires org.slf4j;

    requires dfxengine.dependencies;
    requires dfxengine.entities.api;
    requires dfxengine.handles.api;
    requires dfxengine.handles;
    requires dfxengine.injector.api;
    requires dfxengine.math.api;
    requires dfxengine.math;
    requires dfxengine.text.api;
    requires dfxengine.values.api;
    requires dfxengine.values;
    requires dfxengine.utils;

    exports me.datafox.dfxengine.entities;
    exports me.datafox.dfxengine.entities.data;
    exports me.datafox.dfxengine.entities.definition;
    exports me.datafox.dfxengine.entities.definition.node;
    exports me.datafox.dfxengine.entities.exception;
    exports me.datafox.dfxengine.entities.node;
    exports me.datafox.dfxengine.entities.reference;
    exports me.datafox.dfxengine.entities.utils;
    exports me.datafox.dfxengine.entities.definition.node.supplier;
    exports me.datafox.dfxengine.entities.definition.node.consumer;
    exports me.datafox.dfxengine.entities.definition.node.group;
    exports me.datafox.dfxengine.entities.node.consumer;
    exports me.datafox.dfxengine.entities.node.supplier;
}