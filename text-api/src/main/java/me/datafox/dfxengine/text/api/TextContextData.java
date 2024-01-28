package me.datafox.dfxengine.text.api;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public class TextContextData<T> {
    private final String id;

    private final T defaultValue;

    private final TextContextConverter<T> converter;
}
