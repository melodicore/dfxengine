package me.datafox.dfxengine.text.factory;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextContextData;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author datafox
 */
public class TextContextImpl implements TextContext {
    private final TextFactoryImpl factory;
    private final Map<String,String> params;

    TextContextImpl(TextFactoryImpl factory) {
        this.factory = factory;
        params = new HashMap<>();
    }

    @Override
    public TextFactory getFactory() {
        return factory;
    }

    @Override
    public TextContext copy() {
        return new TextContextImpl(factory).setAll(this, true);
    }

    @Override
    public <T> T get(TextContextData<T> data) {
        return Optional
                .ofNullable(params.get(data.getId()))
                .map(data.getConverter()::toValue)
                .orElse(data.getDefaultValue());
    }

    @Override
    public String getAsString(String id, String defaultValue) {
        return params.getOrDefault(id, defaultValue);
    }

    @Override
    public Map<String,String> getAll() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public <T> TextContext set(TextContextData<T> data, T value) {
        params.put(data.getId(), data.getConverter().toString(value));
        return this;
    }

    @Override
    public TextContext setAll(TextContext other, boolean overwrite) {
        if(overwrite) {
            params.putAll(other.getAll());
        } else {
            other.getAll().forEach(params::putIfAbsent);
        }
        return this;
    }

    @Override
    public <T> TextContext remove(TextContextData<T> data) {
        params.remove(data.getId());
        return this;
    }

    @Override
    public boolean isEmpty() {
        return params.isEmpty();
    }
}
