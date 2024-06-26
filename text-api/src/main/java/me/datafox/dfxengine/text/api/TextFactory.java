package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;

import java.util.List;

/**
 * @author datafox
 */
public interface TextFactory {
    String build(List<Text> texts);

    HandleManager getHandleManager();

    <T> Name<T> createName(T object, String name);

    <T> Name<T> createName(T object, String singular, String plural);

    <T> Name<T> addName(Name<T> name);

    <T> Name<T> getName(T object);

    <T> String getName(T object, boolean plural);

    <T> void addNameConverter(NameConverter<T> converter);

    <T> NameConverter<? super T> getNameConverter(Class<T> type);

    void addNumberFormatter(NumberFormatter formatter);

    NumberFormatter getNumberFormatter(Handle handle);

    NumberFormatter getNumberFormatter(TextConfiguration configuration);

    void addNumberSuffixFactory(NumberSuffixFactory factory);

    NumberSuffixFactory getNumberSuffixFactory(Handle handle);

    NumberSuffixFactory getNumberSuffixFactory(TextConfiguration configuration);

    void setDefaultNumberSuffixFactory(NumberSuffixFactory factory);

    NumberSuffixFactory getDefaultNumberSuffixFactory();

    void setConfiguration(TextConfiguration configuration);

    TextConfiguration getConfiguration();

    void setPluralConverter(PluralConverter converter);

    PluralConverter getPluralConverter();
}
