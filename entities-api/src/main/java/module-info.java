/**
 * @author datafox
 */
open module dfxengine.entities.api {

    requires org.slf4j;

    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.injector.api;
    requires dfxengine.dependencies;
    requires dfxengine.configuration.api;

    exports me.datafox.dfxengine.entities.api.component;
    exports me.datafox.dfxengine.entities.api.condition;
    exports me.datafox.dfxengine.entities.api.data;
    exports me.datafox.dfxengine.entities.api.definition;
    exports me.datafox.dfxengine.entities.api.entity;
    exports me.datafox.dfxengine.entities.api.event;
    exports me.datafox.dfxengine.entities.api.node;
    exports me.datafox.dfxengine.entities.api.reference;
    exports me.datafox.dfxengine.entities.api.state;
}