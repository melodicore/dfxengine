package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.text.api.TextContextConverter;

/**
 * @author datafox
 */
public class StringTextContextConverter implements TextContextConverter<String> {
    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public String toValue(String str) {
        return str;
    }
}
