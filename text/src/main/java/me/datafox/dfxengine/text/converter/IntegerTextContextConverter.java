package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.text.api.TextContextConverter;

/**
 * @author datafox
 */
public class IntegerTextContextConverter implements TextContextConverter<Integer> {
    @Override
    public String toString(Integer value) {
        if(value == null) {
            return "0";
        }
        return value.toString();
    }

    @Override
    public Integer toValue(String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return 0;
        }
    }
}
