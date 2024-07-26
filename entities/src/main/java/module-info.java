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
    requires dfxengine.text.api;
    requires jsonbeans;

    exports me.datafox.dfxengine.entities;
    exports me.datafox.dfxengine.entities.action;
    exports me.datafox.dfxengine.entities.data;
    exports me.datafox.dfxengine.entities.definition;
    exports me.datafox.dfxengine.entities.definition.action;
    exports me.datafox.dfxengine.entities.definition.data;
    exports me.datafox.dfxengine.entities.definition.link;
    exports me.datafox.dfxengine.entities.definition.modifier;
    exports me.datafox.dfxengine.entities.definition.operation;
    exports me.datafox.dfxengine.entities.definition.system;
    exports me.datafox.dfxengine.entities.link;
    exports me.datafox.dfxengine.entities.reference;
    exports me.datafox.dfxengine.entities.reference.data;
    exports me.datafox.dfxengine.entities.reference.selector;
    exports me.datafox.dfxengine.entities.state;
    exports me.datafox.dfxengine.entities.system;
    exports me.datafox.dfxengine.entities.utils;
    opens me.datafox.dfxengine.entities.utils;
    opens me.datafox.dfxengine.entities;
    opens me.datafox.dfxengine.entities.data;
    opens me.datafox.dfxengine.entities.definition;
    opens me.datafox.dfxengine.entities.definition.action;
    opens me.datafox.dfxengine.entities.definition.data;
    opens me.datafox.dfxengine.entities.definition.link;
    opens me.datafox.dfxengine.entities.definition.modifier;
    opens me.datafox.dfxengine.entities.definition.operation;
    opens me.datafox.dfxengine.entities.definition.system;
    opens me.datafox.dfxengine.entities.reference;
    opens me.datafox.dfxengine.entities.reference.data;
    opens me.datafox.dfxengine.entities.reference.selector;
    exports me.datafox.dfxengine.entities.configuration;
    opens me.datafox.dfxengine.entities.configuration;
}