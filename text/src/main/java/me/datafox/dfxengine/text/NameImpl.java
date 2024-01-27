package me.datafox.dfxengine.text;

import lombok.Data;
import me.datafox.dfxengine.text.api.Name;

/**
 * @author datafox
 */
@Data
public final class NameImpl<T> implements Name<T> {
    private final T owner;

    private final String singular;

    private final String plural;
}
