package me.datafox.dfxengine.text.api;

import lombok.Builder;
import lombok.Data;

/**
 * @author datafox
 */
@Data
@Builder
public final class Name<T> {
    private final T owner;

    private final String singular;

    private final String plural;
}
