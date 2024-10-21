/**
 * @author datafox
 */
open module dfxengine.entities.api {
    requires static lombok;

    requires org.slf4j;

    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.injector.api;
    requires dfxengine.dependencies;

    exports me.datafox.dfxengine.entities.api;
    exports me.datafox.dfxengine.entities.api.data;
    exports me.datafox.dfxengine.entities.api.definition;
    exports me.datafox.dfxengine.entities.api.event;
    exports me.datafox.dfxengine.entities.api.node;
}