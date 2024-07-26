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
     * @param text {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String build(Text text) {
        return text.get(this, configuration.copy());
    }

    /**
     * @param texts {@inheritDoc}
     * @return {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param object {@inheritDoc}
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> Name<T> createName(T object, String name) {
        return createName(object, name, pluralConverter.convert(name));
    }

    /**
     * @param object {@inheritDoc}
     * @param singular {@inheritDoc}
     * @param plural {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> addName(Name<T> name) {
        return (Name<T>) names.put(name.getOwner(), name);
    }

    /**
     * @param object {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * @param object {@inheritDoc}
     * @param plural {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> String getName(T object, boolean plural) {
        Name<T> name = getName(object);
        return plural ? name.getPlural() : name.getSingular();
    }

    /**
     * @param converter {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * @param formatter {@inheritDoc}
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
     * @param handle {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public NumberFormatter getNumberFormatter(Handle handle) {
        return numberFormatters.get(handle);
    }

    /**
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public NumberFormatter getNumberFormatter(TextConfiguration configuration) {
        return numberFormatters.get(configuration.get(NUMBER_FORMATTER));
    }

    /**
     * {@inheritDoc}
     *
     * @param formatter {@inheritDoc}
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
     * @param handle {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public NumberSuffixFormatter getNumberSuffixFormatter(Handle handle) {
        return numberSuffixFormatters.get(handle);
    }

    /**
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public NumberSuffixFormatter getNumberSuffixFormatter(TextConfiguration configuration) {
        return numberSuffixFormatters.getOrDefault(
                configuration.get(NUMBER_SUFFIX_FORMATTER),
                getDefaultNumberSuffixFormatter());
    }

    /**
     * {@inheritDoc}
     *
     * @param formatter {@inheritDoc}
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
     * @return {@inheritDoc}
     */
    @Override
    public NumberSuffixFormatter getDefaultNumberSuffixFormatter() {
        return defaultFactory;
    }

    /**
     * {@inheritDoc}
     *
     * @param configuration {@inheritDoc}
     */
    @Override
    public void setConfiguration(TextConfiguration configuration) {
        this.configuration.clear();
        this.configuration.set(configuration);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public TextConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @param converter {@inheritDoc}
     */
    @Override
    public void setPluralConverter(PluralConverter converter) {
        pluralConverter = converter;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public PluralConverter getPluralConverter() {
        return pluralConverter;
    }

    @Initialize
    public void initialize(List<TextConfiguration> configurations) {
        configurations.forEach(this::setConfiguration);
    }
}
