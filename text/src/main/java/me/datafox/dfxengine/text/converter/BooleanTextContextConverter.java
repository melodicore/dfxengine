package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.text.api.TextContextConverter;

/**
 * @author datafox
 */
public class BooleanTextContextConverter implements TextContextConverter<Boolean> {
    @Override
    public String toString(Boolean value) {
        if(value == null) {
            return "false";
        }
        return value.toString();
    }

    @Override
    public Boolean toValue(String str) {
        if("true".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }
}
