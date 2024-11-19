package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.PluralConverter;

/**
 * A {@link PluralConverter} that converts a singular English word into plural form using a basic set of rules. If the
 * word ends with {@code s}, {@code x} or {@code ch}, {@code es} is appended to the end of the singular form. If the
 * word ends with a consonant followed by {@code y}, the {@code y} is removed and {@code ies} is appended to the end. In
 * all other cases, {@code s} is appended to the end.
 *
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class DefaultPluralConverter implements PluralConverter {
    /**
     * Converts a singular noun into its plural form. See {@link DefaultPluralConverter} on how the plural form is
     * determined.
     *
     * @param singular singular form of a noun
     * @return plural form of the provided noun, or {@code null} if the singular is {@code null}
     */
    @Override
    public String convert(String singular) {
        if(singular == null) {
            return null;
        }
        if(singular.matches(".*([sx]|ch)")) {
            return singular + "es";
        }
        if(singular.matches(".*[^aeiouy]y")) {
            return singular.substring(0, singular.length() - 1) + "ies";
        }
        return singular + "s";
    }
}
