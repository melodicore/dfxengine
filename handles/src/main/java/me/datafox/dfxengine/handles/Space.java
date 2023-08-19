package me.datafox.dfxengine.handles;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author datafox
 */
@Data
public final class Space {
    private final String id;

    @Getter(AccessLevel.NONE)
    private final Map<String,Handle> handles;

    Space(String id, Comparator<String> comparator) {
        this.id = id;
        handles = new TreeMap<>(comparator);
    }


}
