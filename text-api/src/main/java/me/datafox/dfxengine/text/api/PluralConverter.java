package me.datafox.dfxengine.text.api;

/**
 * A converter that converts a singular noun to a plural one.
 *
 * @author datafox
 */
@FunctionalInterface
public interface PluralConverter {
    /**
     * @param singular singular form of a noun
     * @return plural form of the provided noun
     */
    String convert(String singular);
}
