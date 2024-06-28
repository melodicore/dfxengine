package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;

import java.util.List;

/**
 * A singleton class that generates {@link String Strings} from {@link Text} objects. It manages {@link Name Names},
 * {@link NameConverter NameConverters}, {@link NumberFormatter NumberFormatters},
 * {@link NumberSuffixFormatter NumberSuffixFacctories}, {@link TextConfiguration} and the {@link PluralConverter}.
 *
 * @author datafox
 */
public interface TextFactory {
    /**
     * @param text {@link Text} object to build
     * @return {@link String} representation of the text
     */
    String build(Text text);

    /**
     * @param texts {@link Text} objects to build
     * @return {@link String} representation of the texts
     */
    String build(List<Text> texts);

    /**
     * @return {@link HandleManager} used by this factory
     */
    HandleManager getHandleManager();

    /**
     * The {@link PluralConverter} is used to generate the plural form.
     *
     * @param object object to be named
     * @param name name for the object in singular form
     * @return {@link Name} associated with the object
     * @param <T> type of the object
     */
    <T> Name<T> createName(T object, String name);

    /**
     * @param object object to be named
     * @param singular name for the object in singular form
     * @param plural name for the object in plural form
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    <T> Name<T> createName(T object, String singular, String plural);

    /**
     * @param name {@link Name} object
     * @return previous {@link Name} associated with {@link Name#getOwner()} or {@code null} if no previous mapping is
     * present
     * @param <T> type of the object to be named
     */
    <T> Name<T> addName(Name<T> name);

    /**
     * If a {@link Name} associated with the object is present, it is returned. Otherwise, a new Name is created using
     * {@link NameConverter} if a valid one is present or {@link Object#toString()} if not. The {@link PluralConverter}
     * will be used for the plural form if the {@link NameConverter} is not plural capable or not present.
     *
     * @param object object to be named
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    <T> Name<T> getName(T object);

    /**
     * If a {@link Name} associated with the object is present, it is used. Otherwise, a new Name is created using
     * {@link NameConverter} if a valid one is present or {@link Object#toString()} if not. The {@link PluralConverter}
     * will be used for the plural form if the {@link NameConverter} is not plural capable or not present.
     *
     * @param object object to be named
     * @param plural if {@code true}, the plural form is returned
     * @return name of the object
     * @param <T> type of the object to be named
     */
    <T> String getName(T object, boolean plural);

    /**
     * @param converter {@link NameConverter} to be registered
     * @param <T> type of the object that this {@link NameConverter} is capable of converting
     */
    <T> void addNameConverter(NameConverter<T> converter);

    /**
     * @param type {@link Class} of the object to be named
     * @return {@link NameConverter} associated with the type or any of its interfaces or superclasses
     * @param <T> type of the object to be named
     */
    <T> NameConverter<T> getNameConverter(Class<T> type);

    /**
     * @param formatter {@link NumberFormatter} to be registered
     */
    void addNumberFormatter(NumberFormatter formatter);

    /**
     * @param handle {@link Handle} of a {@link NumberFormatter}
     * @return {@link NumberFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    NumberFormatter getNumberFormatter(Handle handle);

    /**
     * @param configuration {@link TextConfiguration} to be used
     * @return {@link NumberFormatter} configured in the {@link TextConfiguration} or {@code null} if none is present
     */
    NumberFormatter getNumberFormatter(TextConfiguration configuration);

    /**
     * If no default {@link NumberSuffixFormatter} is set and this {@link NumberSuffixFormatter} can format any number
     * ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}), it will be set as default.
     *
     * @param formatter {@link NumberSuffixFormatter} to be registered
     */
    void addNumberSuffixFormatter(NumberSuffixFormatter formatter);

    /**
     * @param handle {@link Handle} of a {@link NumberSuffixFormatter}
     * @return {@link NumberSuffixFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    NumberSuffixFormatter getNumberSuffixFormatter(Handle handle);

    /**
     * @param configuration {@link TextConfiguration} to be used
     * @return {@link NumberSuffixFormatter} configured in the {@link TextConfiguration} or {@code null} if none is present
     */
    NumberSuffixFormatter getNumberSuffixFormatter(TextConfiguration configuration);

    /**
     * The default {@link NumberSuffixFormatter} must be capable of formatting any number
     * ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}).
     *
     * @param formatter {@link NumberSuffixFormatter} to be set as default
     */
    void setDefaultNumberSuffixFormatter(NumberSuffixFormatter formatter);

    /**
     * @return default {@link NumberSuffixFormatter} or {@code null} if none is present
     */
    NumberSuffixFormatter getDefaultNumberSuffixFormatter();

    /**
     * Clears current {@link TextConfiguration} and applies the provided one to it. This method should not overwrite the
     * current {@link TextConfiguration} instance and only alter its state.
     *
     * @param configuration {@link TextConfiguration} to be applied
     */
    void setConfiguration(TextConfiguration configuration);

    /**
     * This method should always return the same {@link TextConfiguration} instance.
     *
     * @return current {@link TextConfiguration}
     */
    TextConfiguration getConfiguration();

    /**
     * @param converter {@link PluralConverter} to be registered
     */
    void setPluralConverter(PluralConverter converter);

    /**
     * @return registered {@link PluralConverter} or {@code null} if none is present
     */
    PluralConverter getPluralConverter();
}
