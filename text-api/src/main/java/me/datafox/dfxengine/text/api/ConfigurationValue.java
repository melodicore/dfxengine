package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface ConfigurationValue<T> {
    T get(TextFactory factory, TextConfiguration configuration);
}
