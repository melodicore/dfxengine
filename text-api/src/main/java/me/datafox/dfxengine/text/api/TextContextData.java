package me.datafox.dfxengine.text.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author datafox
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TextContextData<T> {
    @EqualsAndHashCode.Include
    private final String id;

    private final T defaultValue;

    private final TextContextConverter<T> converter;
}
