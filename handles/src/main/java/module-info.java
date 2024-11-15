/**
 * Handles is a module for creating and managing dynamic identifier handles.
 *
 * @author datafox
 */
module dfxengine.handles {
    requires static lombok;

    requires org.slf4j;

    requires dfxengine.utils;
    requires dfxengine.injector.api;
    requires dfxengine.handles.api;

    exports me.datafox.dfxengine.handles;
}