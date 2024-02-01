/**
 * @author datafox
 */

module dfxengine.injector {
    requires static lombok;

    requires io.github.classgraph;
    requires org.slf4j;

    requires dfxengine.utils;
    requires dfxengine.collections;
    requires dfxengine.injector.api;

    exports me.datafox.dfxengine.injector;
    exports me.datafox.dfxengine.injector.internal;
}