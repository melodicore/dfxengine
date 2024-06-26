package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.PluralConverter;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class DefaultPluralConverter implements PluralConverter {
    @Override
    public String convert(String singular) {
        if(singular.matches(".*([sx]|ch)$")) {
            return singular + "es";
        }
        if(singular.matches(".*[^aeiouy]y$")) {
            return singular.substring(0, singular.length() - 1) + "ies";
        }
        return singular + "s";
    }
}
