package me.datafox.dfxengine.text.api;

/**
 * A converter that converts a singular noun to a plural one.
 *
 * @author datafox
 */
@FunctionalInterface
public interface PluralConverter {
    /**
     * Converts a singular noun into its plural form.
     *
     * @param singular singular form of a noun
     * @return plural form of the provided noun, or {@code null} if the singular is {@code null}
     */
    String convert(String singular);
}
