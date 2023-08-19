/**
 * @author datafox
 */

module dfxengine.injector {
    requires dfxengine.utils;

    requires static lombok;

    requires io.github.classgraph;
    requires org.slf4j;
    requires dfxengine.injector.api;

    exports me.datafox.dfxengine.injector;
}