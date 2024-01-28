package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.text.api.TextContextConverter;

/**
 * @author datafox
 */
public class BooleanTextContextConverter implements TextContextConverter<Boolean> {
    @Override
    public String toString(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean toValue(String str) {
        if("true".equalsIgnoreCase(str)) {
            return true;
        }
        if("false".equalsIgnoreCase(str)) {
            return false;
        }
        return null;
    }
}
