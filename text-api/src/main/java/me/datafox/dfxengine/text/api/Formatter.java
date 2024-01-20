package me.datafox.dfxengine.text.api;

/**
 * A functional interface that formats an object into a {@link String}.
 *
 * @author datafox
 */
public interface Formatter<T> {
    /**
     * @param object object to be formatted
     * @return {@link String} representation for the specified object
     */
    String format(T object);
}
