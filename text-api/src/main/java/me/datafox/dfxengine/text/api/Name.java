package me.datafox.dfxengine.text.api;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public final class Name<T> {
    private final T owner;

    private final String singular;

    private final String plural;
}
