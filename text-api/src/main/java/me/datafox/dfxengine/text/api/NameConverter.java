package me.datafox.dfxengine.text.api;

/**
 * A converter that converts an object of type {@link T} to a singular form, and optionally also a plural form. If this
 * converter does not support plural forms, the {@link PluralConverter} will be used instead.
 *
 * @author datafox
 */
public interface NameConverter<T> {
    /**
     * @return type of the object that this converter can convert
     */
    Class<T> getType();

    /**
     * @return {@code true} if this converter can also convert to plural form
     */
    boolean isPluralCapable();

    /**
     * @param object object to be converted
     * @return name of the object in singular form
     */
    String convert(T object);

    /**
     * @param object object to be converted
     * @return name of the object in singular form, or {@code null} if this converter does not support plural form
     */
    String convertPlural(T object);
}
