/**
 * @author datafox
 */
module dfxengine.text {
    requires static lombok;

    requires org.slf4j;
    requires ch.obermuhlner.math.big;

    requires dfxengine.injector.api;
    requires dfxengine.text.api;
    requires dfxengine.dependencies;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.handles.api;
    requires dfxengine.handles;
    requires dfxengine.utils;
}