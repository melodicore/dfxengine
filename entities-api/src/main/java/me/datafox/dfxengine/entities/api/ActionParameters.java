package me.datafox.dfxengine.entities.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author datafox
 */
public final class ActionParameters {
    private final Map<Key<?>,Object> parameters;

    public ActionParameters() {
        parameters = new HashMap<>();
    }

    public <T> ActionParameters put(Key<T> key, T value) {
        parameters.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) parameters.getOrDefault(key, key.getDefaultValue());
    }

    @Override
    public String toString() {
        return parameters.toString();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Key<T> {
        private final String id;
        private final T defaultValue;

        @Override
        public String toString() {
            return id;
        }
    }

    public static <T> Key<T> key(String id, T defaultValue) {
        return new Key<>(id, defaultValue);
    }
}
