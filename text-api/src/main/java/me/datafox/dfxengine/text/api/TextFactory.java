package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;

import java.util.List;

/**
 * A singleton class that generates {@link String Strings} from {@link Text} objects. It manages {@link Name Names},
 * {@link NameConverter NameConverters}, {@link NumberFormatter NumberFormatters},
 * {@link NumberSuffixFormatter NumberSuffixFacctories}, {@link Configuration} and the {@link PluralConverter}.
 *
 * @author datafox
 */
public interface TextFactory {
    /**
     * Builds a {@link Text} object into a {@link String}.
     *
     * @param text {@link Text} object to build
     * @return {@link String} representation of the text
     */
    String build(Text text);

    /**
     * Builds a list of {@link Text} objects into a {@link String}.
     *
     * @param texts {@link Text} objects to build
     * @return {@link String} representation of the texts
     */
    String build(List<Text> texts);

    /**
     * Returns the {@link ConfigurationManager} used by this factory.
     *
     * @return {@link ConfigurationManager} used by this factory
     */
    ConfigurationManager getConfigurationManager();

    /**
     * Returns the {@link HandleManager} used by this factory.
     *
     * @return {@link HandleManager} used by this factory
     */
    HandleManager getHandleManager();

    /**
     * Creates and registers a {@link Name} for the specified object. The {@link PluralConverter} is used to generate
     * the plural form.
     *
     * @param object object to be named
     * @param name name for the object in singular form
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    <T> Name<T> createName(T object, String name);

    /**
     * Creates and registers a {@link Name} for the specified object.
     *
     * @param object object to be named
     * @param singular name for the object in singular form
     * @param plural name for the object in plural form
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    <T> Name<T> createName(T object, String singular, String plural);

    /**
     * Registers a {@link Name}.
     *
     * @param name {@link Name} object
     * @return previous {@link Name} associated with the owner, or {@code null} if no previous mapping is present
     * @param <T> type of the object to be named
     */
    <T> Name<T> addName(Name<T> name);

    /**
     * Returns the singular name of the specified object. If a {@link Name} associated with the object is present, it is
     * returned. Otherwise, a new Name is created using {@link NameConverter} if a valid one is present or
     * {@link Object#toString()} if not. The {@link PluralConverter} will be used for the plural form if the
     * {@link NameConverter} is not plural capable or not present.
     *
     * @param object object to be named
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    <T> Name<T> getName(T object);

    /**
     * Returns the name of the specified object. If a {@link Name} associated with the object is present, it is used.
     * Otherwise, a new Name is created using {@link NameConverter} if a valid one is present or
     * {@link Object#toString()} if not. The {@link PluralConverter} will be used for the plural form if the
     * {@link NameConverter} is not plural capable or not present.
     *
     * @param object object to be named
     * @param plural if {@code true}, the plural form is returned
     * @return name of the object
     * @param <T> type of the object to be named
     */
    <T> String getName(T object, boolean plural);

    /**
     * Registers a {@link NameConverter}.
     *
     * @param converter {@link NameConverter} to be registered
     * @param <T> type of the object that this {@link NameConverter} is capable of converting
     */
    <T> void addNameConverter(NameConverter<T> converter);

    /**
     * Returns a {@link NameConverter} for the specified type.
     *
     * @param type {@link Class} of the object to be named
     * @return {@link NameConverter} associated with the type or any of its interfaces or superclasses
     * @param <T> type of the object to be named
     */
    <T> NameConverter<T> getNameConverter(Class<T> type);

    /**
     * Registers a {@link NumberFormatter}.
     *
     * @param formatter {@link NumberFormatter} to be registered
     */
    void addNumberFormatter(NumberFormatter formatter);

    /**
     * Returns the {@link NumberFormatter} with the specified {@link Handle}.
     *
     * @param handle {@link Handle} of a {@link NumberFormatter}
     * @return {@link NumberFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    NumberFormatter getNumberFormatter(Handle handle);

    /**
     * Returns the {@link NumberFormatter} associated with the specified {@link Configuration}.
     *
     * @param configuration {@link Configuration} to be used
     * @return {@link NumberFormatter} configured in the {@link Configuration} or {@code null} if none is present
     */
    NumberFormatter getNumberFormatter(Configuration configuration);

    /**
     * Registers a {@link NumberSuffixFormatter}. If no default suffix formatter is set and the specified suffix
     * formatter can format any number ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}), it will be set
     * as default.
     *
     * @param formatter {@link NumberSuffixFormatter} to be registered
     */
    void addNumberSuffixFormatter(NumberSuffixFormatter formatter);

    /**
     * Returns the {@link NumberSuffixFormatter} with the specified {@link Handle}.
     *
     * @param handle {@link Handle} of a {@link NumberSuffixFormatter}
     * @return {@link NumberSuffixFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    NumberSuffixFormatter getNumberSuffixFormatter(Handle handle);

    /**
     * Returns the {@link NumberSuffixFormatter} associated with the specified {@link Configuration}.
     *
     * @param configuration {@link Configuration} to be used
     * @return {@link NumberSuffixFormatter} configured in the {@link Configuration} or {@code null} if none is
     * present
     */
    NumberSuffixFormatter getNumberSuffixFormatter(Configuration configuration);

    /**
     * Sets the default {@link NumberSuffixFormatter}. The default suffix formatter must be capable of formatting any
     * number ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}).
     *
     * @param formatter {@link NumberSuffixFormatter} to be set as default
     */
    void setDefaultNumberSuffixFormatter(NumberSuffixFormatter formatter);

    /**
     * Returns the default {@link NumberSuffixFormatter}.
     *
     * @return default {@link NumberSuffixFormatter} or {@code null} if none is present
     */
    NumberSuffixFormatter getDefaultNumberSuffixFormatter();

    /**
     * Registers the {@link PluralConverter} to be used.
     *
     * @param converter {@link PluralConverter} to be registered
     */
    void setPluralConverter(PluralConverter converter);

    /**
     * Returns the registered {@link PluralConverter} or {@code null} if none is present.
     *
     * @return registered {@link PluralConverter} or {@code null} if none is present
     */
    PluralConverter getPluralConverter();
}
