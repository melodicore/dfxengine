package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author datafox
 */
public interface TextFactory {
    String build(Context initialContext, TextDefinition definition);

    Space getContextSpace();

    Space getNumberFormatterSpace();

    void setDefaultContext(Context context);

    Context getDefaultContext();

    Context createEmptyContext();

    <T> void registerName(T object, String singular, String plural);

    <T> Name<T> getName(T object);

    void setPluralConverter(Function<String,String> pluralConverter);

    Function<String,String> getPluralConverter();

    void registerNumberFormatter(NumberFormatter numberFormatter);

    Optional<NumberFormatter> getNumberFormatter(Handle key);

    default String build(TextDefinition definition) {
        return build(createEmptyContext(), definition);
    }

    default Optional<NumberFormatter> getNumberFormatterById(String key) {
        return getNumberFormatter(getNumberFormatterSpace().getOrCreateHandle(key));
    }

    interface Context {
        TextFactory getFactory();

        Space getSpace();

        Context copy();

        Map<Handle,String> getAll();

        Context setAll(Context other, boolean overwrite);

        String get(Handle key, String defaultValue);

        boolean get(Handle key, boolean defaultValue);

        int get(Handle key, int defaultValue);

        Context set(Handle key, String value);

        Context set(Handle key, boolean value);

        Context set(Handle key, int value);

        boolean isEmpty();

        default String getById(String id, String defaultValue) {
            return get(getSpace().getOrCreateHandle(id), defaultValue);
        }

        default boolean getById(String id, boolean defaultValue) {
            return get(getSpace().getOrCreateHandle(id), defaultValue);
        }

        default int getById(String id, int defaultValue) {
            return get(getSpace().getOrCreateHandle(id), defaultValue);
        }

        default Context setById(String id, String value) {
            return set(getSpace().getOrCreateHandle(id), value);
        }

        default Context setById(String id, boolean value) {
            return set(getSpace().getOrCreateHandle(id), value);
        }

        default Context setById(String id, int value) {
            return set(getSpace().getOrCreateHandle(id), value);
        }
    }
}
