/**
 * Text generation, manipulation and representation.
 *
 * @author datafox
 */
module dfxengine.text {
    requires static lombok;

    requires org.slf4j;
    requires ch.obermuhlner.math.big;

    requires dfxengine.handles.api;
    requires dfxengine.injector.api;
    requires dfxengine.text.api;
    requires dfxengine.handles;
    requires dfxengine.math.api;
    requires dfxengine.values.api;
    requires dfxengine.utils;

    exports me.datafox.dfxengine.text;
    exports me.datafox.dfxengine.text.converter;
    exports me.datafox.dfxengine.text.formatter;
    exports me.datafox.dfxengine.text.suffix;
    exports me.datafox.dfxengine.text.text;
    exports me.datafox.dfxengine.text.utils;
}