/**
 * @author datafox
 */
module dfxengine.math {
    requires static lombok;

    requires org.slf4j;
    requires ch.obermuhlner.math.big;

    requires dfxengine.math.api;

    exports me.datafox.dfxengine.math.numeral;
    exports me.datafox.dfxengine.math.utils;
}