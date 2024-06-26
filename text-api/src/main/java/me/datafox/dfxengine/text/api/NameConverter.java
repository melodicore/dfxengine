package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface NameConverter<T> {
    Class<T> getType();

    boolean isPluralCapable();

    String convert(T object);

    String convertPlural(T object);
}
