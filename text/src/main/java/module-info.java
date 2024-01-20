/**
 * @author datafox
 */
module dfxengine.text {
    requires static lombok;

    requires org.slf4j;

    requires dfxengine.text.api;
    requires dfxengine.dependencies;
    requires dfxengine.handles.api;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.utils;

    exports me.datafox.dfxengine.text.text;
}