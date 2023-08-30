package me.datafox.dfxengine.utils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilities for manipulating maps.
 *
 * @author datafox
 */
public class MapUtils {
    /**
     * Reverses a map.
     *
     * @param map map to be reversed
     * @return map with all keys and values reversed
     *
     * @throws IllegalArgumentException if the values of the map are not all distinct
     */
    public static <K,V> Map<V,K> reverseMap(Map<K,V> map) {
        if(map.values().stream().distinct().count() != map.size()) {
            throw new IllegalArgumentException("Not all values of the map are distinct. Map reversal is not possible.");
        }

        return map
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey));
    }
}
