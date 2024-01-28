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

    <T> void set(T value, TextContextData<T> data);

    TextContext setAll(TextContext other, boolean overwrite);

    boolean isEmpty();

    TextContext clear();
}
