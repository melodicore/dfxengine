package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface NameConverter<T> {
    String getName(T value);

    Class<T> getObjectClass();
}
