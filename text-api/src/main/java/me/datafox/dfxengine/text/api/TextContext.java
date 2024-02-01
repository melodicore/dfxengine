package me.datafox.dfxengine.text.api;

import java.util.Map;

/**
 * @author datafox
 */
public interface TextContext {
    TextFactory getFactory();

    TextContext copy();

    <T> T get(TextContextData<T> data);

    String getAsString(String id, String defaultValue);

    Map<String,String> getAll();

    <T> TextContext set(TextContextData<T> data, T value);

    TextContext setAll(TextContext other, boolean overwrite);

    <T> TextContext remove(TextContextData<T> data);

    boolean isEmpty();
}
