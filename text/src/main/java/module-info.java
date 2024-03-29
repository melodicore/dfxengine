/**
 * @author datafox
 */
module dfxengine.text {
    requires static lombok;

    requires org.slf4j;

    requires dfxengine.injector.api;
    requires dfxengine.text.api;
    requires dfxengine.dependencies;
    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.utils;
    requires dfxengine.collections;

    exports me.datafox.dfxengine.text.definition;
    exports me.datafox.dfxengine.text.formatter;
    exports me.datafox.dfxengine.text.factory;
    exports me.datafox.dfxengine.text.utils;
    exports me.datafox.dfxengine.text.converter;
}