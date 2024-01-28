package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface TextContextConverter<T> {
    String toString(T value);

    T toValue(String str);
}
