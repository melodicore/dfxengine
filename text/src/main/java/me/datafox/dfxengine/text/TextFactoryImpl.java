package me.datafox.dfxengine.text;

import lombok.Getter;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.TextConfigurationImpl;
import me.datafox.dfxengine.text.utils.TextHandles;
import me.datafox.dfxengine.text.utils.internal.TextStrings;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.text.utils.ConfigurationKeys.*;

/**
 * Implementation of {@link TextFactory}, a singleton class that generates {@link String Strings} from {@link Text}
 * objects. It manages {@link Name Names}, {@link NameConverter NameConverters},
 * {@link NumberFormatter NumberFormatters}, {@link NumberSuffixFormatter NumberSuffixFacctories},
 * {@link TextConfiguration} and the {@link PluralConverter}. This class is designed to be used with the
 * {@link Injector}.
 *
 * @author datafox
 */
@Component
public class TextFactoryImpl implements TextFactory {
    private final Logger logger;

    /**
     * {@link HandleManager} used by this factory.
     */
    @Getter
    private final HandleManager handleManager;
    private final Map<Object, Name<?>> names;
    private final Map<Class<?>,NameConverter<?>> nameConverters;
    private final HandleMap<NumberFormatter> numberFormatters;
    private final HandleMap<NumberSuffixFormatter> numberSuffixFormatters;
    private final TextConfiguration configuration;
    private NumberSuffixFormatter defaultFactory;
    private PluralConverter pluralConverter;

    /**
     * Public constructor for {@link TextFactoryImpl}.
     *
     * @param logger {@link Logger} for this factory
     * @param handleManager {@link HandleManager} for this factory
     * @param handles {@link TextHandles} for this factory
     * @param names {@link Name Names} for this factory
     * @param nameConverters {@link NameConverter NameConverters} for this factory
     * @param numberFormatters {@link NumberFormatter NumberFormatters} for this factory
     * @param numberSuffixFormatters {@link NumberSuffixFormatter NumberSuffixFormatters} for this factory
     * @param pluralConverter {@link PluralConverter} for this factory
     */
    @Inject
    public TextFactoryImpl(Logger logger,
                           HandleManager handleManager,
                           TextHandles handles,
                           List<Name<?>> names,
                           List<NameConverter<?>> nameConverters,
                           List<NumberFormatter> numberFormatters,
                           List<NumberSuffixFormatter> numberSuffixFormatters,
                           PluralConverter pluralConverter) {
        this.logger = logger;
        this.handleManager = handleManager;
        this.names = new HashMap<>();
        this.nameConverters = new HashMap<>();
        this.numberFormatters = new HashHandleMap<>(handles.getNumberFormatters());
        this.numberSuffixFormatters = new HashHandleMap<>(handles.getNumberSuffixFormatters());
        this.configuration = new TextConfigurationImpl(this);
        this.pluralConverter = pluralConverter;

        names.forEach(this::addName);
        nameConverters.forEach(this::addNameConverter);
        numberFormatters.forEach(this::addNumberFormatter);
        numberSuffixFormatters.forEach(this::addNumberSuffixFormatter);
    }

    /**
     * Builds a {@link Text} object into a {@link String}.
     *
     * @param text {@link Text} object to build
     * @return {@link String} representation of the text
     */
    @Override
    public String build(Text text) {
        return text.get(this, configuration.copy());
    }

    /**
     * Builds a list of {@link Text} objects into a {@link String}.
     *
     * @param texts {@link Text} objects to build
     * @return {@link String} representation of the texts
     */
    @Override
    public String build(List<Text> texts) {
        TextConfiguration effective = configuration.copy();
        String delimiter = effective.get(DELIMITER);
        return texts
                .stream()
                .map(t -> t.get(this, effective))
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Creates and registers a {@link Name} for the specified object. The {@link PluralConverter} is used to generate
     * the plural form.
     *
     * @param object object to be named
     * @param name name for the object in singular form
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    @Override
    public <T> Name<T> createName(T object, String name) {
        return createName(object, name, pluralConverter.convert(name));
    }

    /**
     * Creates and registers a {@link Name} for the specified object.
     *
     * @param object object to be named
     * @param singular name for the object in singular form
     * @param plural name for the object in plural form
     * @return {@link Name} associated with the object
     * @param <T> type of the object to be named
     */
    @Override
    public <T> Name<T> createName(T object, String singular, String plural) {
        Name<T> name = Name
                .<T>builder()
                .owner(object)
                .singular(singular)
                .plural(plural)
                .build();
        addName(name);
        return name;
    }

    /**
     * Registers a {@link Name}.
     *
     * @param name {@link Name} object
     * @return previous {@link Name} associated with the owner, or {@code null} if no previous mapping is present
     * @param <T> type of the object to be named
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> addName(Name<T> name) {
        return (Name<T>) names.put(name.getOwner(), name);
    }

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
    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> getName(T object) {
        if(names.containsKey(object)) {
            return (Name<T>) names.get(object);
        }
        if(object instanceof Named) {
            return ((Named<T>) object).getName();
        }
        String singular;
        String plural = null;
        NameConverter<? super T> converter = (NameConverter<? super T>) getNameConverter(object.getClass());
        if(converter == null) {
            singular = object.toString();
        } else {
            singular = converter.convert(object);
            if(converter.isPluralCapable()) {
                plural = converter.convertPlural(object);
            }
        }
        if(plural == null) {
            plural = pluralConverter.convert(singular);
        }
        return createName(object, singular, plural);
    }

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
    @Override
    public <T> String getName(T object, boolean plural) {
        Name<T> name = getName(object);
        return plural ? name.getPlural() : name.getSingular();
    }

    /**
     * Registers a {@link NameConverter}.
     *
     * @param converter {@link NameConverter} to be registered
     * @param <T> type of the object that this {@link NameConverter} is capable of converting
     */
    @Override
    public <T> void addNameConverter(NameConverter<T> converter) {
        if(nameConverters.containsKey(converter.getType())) {
            logger.warn(TextStrings.converterPresent(converter));
            return;
        }
        nameConverters.put(converter.getType(), converter);
    }

    /**
     * Returns a {@link NameConverter} for the specified type.
     *
     * @param type {@link Class} of the object to be named
     * @return {@link NameConverter} associated with the type or any of its interfaces or superclasses
     * @param <T> type of the object to be named
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> NameConverter<T> getNameConverter(Class<T> type) {
        if(nameConverters.containsKey(type)) {
            return (NameConverter<T>) nameConverters.get(type);
        }
        if(Object.class.equals(type)) {
            return null;
        }
        Optional<NameConverter<?>> converter = Arrays
                .stream(type.getInterfaces())
                .filter(nameConverters::containsKey)
                .findFirst()
                .map(nameConverters::get);
        return (NameConverter<T>) converter.orElseGet(() -> getNameConverter(type.getSuperclass()));
    }

    /**
     * Registers a {@link NumberFormatter}.
     *
     * @param formatter {@link NumberFormatter} to be registered
     */
    @Override
    public void addNumberFormatter(NumberFormatter formatter) {
        if(numberFormatters.containsKey(formatter.getHandle())) {
            logger.warn(TextStrings.formatterPresent(formatter));
            return;
        }
        numberFormatters.put(formatter.getHandle(), formatter);
    }

    /**
     * Returns the {@link NumberFormatter} with the specified {@link Handle}.
     *
     * @param handle {@link Handle} of a {@link NumberFormatter}
     * @return {@link NumberFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    @Override
    public NumberFormatter getNumberFormatter(Handle handle) {
        return numberFormatters.get(handle);
    }

    /**
     * Returns the {@link NumberFormatter} associated with the specified {@link TextConfiguration}.
     *
     * @param configuration {@link TextConfiguration} to be used
     * @return {@link NumberFormatter} configured in the {@link TextConfiguration} or {@code null} if none is present
     */
    @Override
    public NumberFormatter getNumberFormatter(TextConfiguration configuration) {
        return numberFormatters.get(configuration.get(NUMBER_FORMATTER));
    }

    /**
     * Registers a {@link NumberSuffixFormatter}. If no default suffix formatter is set and the specified suffix
     * formatter can format any number ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}), it will be set
     * as default.
     *
     * @param formatter {@link NumberSuffixFormatter} to be registered
     */
    @Override
    public void addNumberSuffixFormatter(NumberSuffixFormatter formatter) {
        if(numberSuffixFormatters.containsKey(formatter.getHandle())) {
            logger.warn(TextStrings.suffixFormatterPresent(formatter));
            return;
        }
        if(formatter.isInfinite() && defaultFactory == null) {
            defaultFactory = formatter;
        }
        numberSuffixFormatters.put(formatter.getHandle(), formatter);
    }

    /**
     * Returns the {@link NumberSuffixFormatter} with the specified {@link Handle}.
     *
     * @param handle {@link Handle} of a {@link NumberSuffixFormatter}
     * @return {@link NumberSuffixFormatter} associated with the {@link Handle} or {@code null} if none is present
     */
    @Override
    public NumberSuffixFormatter getNumberSuffixFormatter(Handle handle) {
        return numberSuffixFormatters.get(handle);
    }

    /**
     * Returns the {@link NumberSuffixFormatter} associated with the specified {@link TextConfiguration}.
     *
     * @param configuration {@link TextConfiguration} to be used
     * @return {@link NumberSuffixFormatter} configured in the {@link TextConfiguration} or {@code null} if none is
     * present
     */
    @Override
    public NumberSuffixFormatter getNumberSuffixFormatter(TextConfiguration configuration) {
        return numberSuffixFormatters.getOrDefault(
                configuration.get(NUMBER_SUFFIX_FORMATTER),
                getDefaultNumberSuffixFormatter());
    }

    /**
     * Sets the default {@link NumberSuffixFormatter}. The default suffix formatter must be capable of formatting any
     * number ({@link NumberSuffixFormatter#isInfinite()} returns {@code true}).
     *
     * @param formatter {@link NumberSuffixFormatter} to be set as default
     */
    @Override
    public void setDefaultNumberSuffixFormatter(NumberSuffixFormatter formatter) {
        if(!formatter.isInfinite()) {
            logger.warn(TextStrings.notInfinite(formatter));
            return;
        }
        defaultFactory = formatter;
    }

    /**
     * Returns the default {@link NumberSuffixFormatter}.
     *
     * @return default {@link NumberSuffixFormatter} or {@code null} if none is present
     */
    @Override
    public NumberSuffixFormatter getDefaultNumberSuffixFormatter() {
        return defaultFactory;
    }

    /**
     * Clears current {@link TextConfiguration} and applies the provided one to it. This method should not overwrite the
     * current {@link TextConfiguration} instance and only alter its state.
     *
     * @param configuration {@link TextConfiguration} to be applied
     */
    @Override
    public void setConfiguration(TextConfiguration configuration) {
        this.configuration.clear();
        this.configuration.set(configuration);
    }

    /**
     * This method should always return the same {@link TextConfiguration} instance.
     *
     * @return current {@link TextConfiguration}
     */
    @Override
    public TextConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Registers the {@link PluralConverter} to be used.
     *
     * @param converter {@link PluralConverter} to be registered
     */
    @Override
    public void setPluralConverter(PluralConverter converter) {
        pluralConverter = converter;
    }

    /**
     * Returns the registered {@link PluralConverter} or {@code null} if none is present.
     *
     * @return registered {@link PluralConverter} or {@code null} if none is present
     */
    @Override
    public PluralConverter getPluralConverter() {
        return pluralConverter;
    }

    /**
     * Registers the specified {@link TextConfiguration TextConfigurations} to this factory.
     *
     * @param configurations {@link TextConfiguration TextConfigurations} to be registered
     */
    @Initialize
    public void initialize(List<TextConfiguration> configurations) {
        configurations.forEach(this::setConfiguration);
    }
}
