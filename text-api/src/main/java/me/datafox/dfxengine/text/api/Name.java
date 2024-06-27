package me.datafox.dfxengine.text.api;

import lombok.Builder;
import lombok.Data;

/**
 * A data class that wraps an object and its assigned name in singular and plural form.
 *
 * @author datafox
 */
@Data
@Builder
public final class Name<T> {
    /**
     * Named object.
     */
    private final T owner;

    /**
     * Name of {@link #getOwner()} in singular form.
     */
    private final String singular;

    /**
     * Name of {@link #getOwner()} in plural form.
     */
    private final String plural;
}
