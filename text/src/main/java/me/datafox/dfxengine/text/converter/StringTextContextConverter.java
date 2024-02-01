package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.text.api.TextContextConverter;

/**
 * @author datafox
 */
public class StringTextContextConverter implements TextContextConverter<String> {
    @Override
    public String toString(String value) {
        if(value == null) {
            return "";
        }
        return value;
    }

    @Override
    public String toValue(String str) {
        if(str == null) {
            return "";
        }
        return str;
    }
}
