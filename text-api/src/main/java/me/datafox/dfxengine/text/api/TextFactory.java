package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author datafox
 */
public interface TextFactory {
    String build(TextContext initialContext, TextDefinition definition);

    Space getNumberFormatterSpace();

    void setDefaultContext(TextContext context);

    TextContext getDefaultContext();

    TextContext createEmptyContext();

    <T> void registerName(T object, String singular, String plural);

    <T> Name<T> getName(T object);

    <T> void registerNameConverter(NameConverter<T> nameConverter);

    <T> NameConverter<T> getNameConverter(Class<T> type);

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
}
