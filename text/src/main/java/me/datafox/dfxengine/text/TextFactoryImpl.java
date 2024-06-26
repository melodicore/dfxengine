package me.datafox.dfxengine.text;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.TextConfigurationImpl;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.text.utils.ConfigurationKeys.*;

/**
 * @author datafox
 */
@Component
public class TextFactoryImpl implements TextFactory {
    private final Logger logger;
    @Getter
    private final HandleManager handleManager;
    private final Map<Object, Name<?>> names;
    private final Map<Class<?>,NameConverter<?>> converters;
    private final Map<Handle,NumberFormatter> formatters;
    private final Map<Handle,NumberSuffixFactory> factories;
    private final TextConfiguration configuration;
    private NumberSuffixFactory defaultFactory;
    private PluralConverter pluralConverter;

    @Inject
    public TextFactoryImpl(Logger logger,
                           HandleManager handleManager,
                           List<Name<?>> names,
                           List<NameConverter<?>> converters,
                           List<NumberFormatter> formatters,
                           List<NumberSuffixFactory> factories,
                           PluralConverter pluralConverter) {
        this.logger = logger;
        this.handleManager = handleManager;
        this.names = new HashMap<>();
        this.converters = new HashMap<>();
        this.formatters = new HashMap<>();
        this.factories = new HashMap<>();
        this.configuration = new TextConfigurationImpl(this);
        this.pluralConverter = pluralConverter;

        names.forEach(this::addName);
        converters.forEach(this::addNameConverter);
        formatters.forEach(this::addNumberFormatter);
        factories.forEach(this::addNumberSuffixFactory);
    }

    @Override
    public String build(List<Text> texts) {
        TextConfiguration effective = configuration.copy();
        String delimiter = effective.get(DELIMITER);
        return texts
                .stream()
                .map(t -> t.get(this, effective))
                .collect(Collectors.joining(delimiter));
    }

    @Override
    public <T> Name<T> createName(T object, String name) {
        return createName(object, name, pluralConverter.convert(name));
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> addName(Name<T> name) {
        return (Name<T>) names.put(name.getOwner(), name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> getName(T object) {
        if(names.containsKey(object)) {
            return (Name<T>) names.get(object);
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

    @Override
    public <T> String getName(T object, boolean plural) {
        Name<T> name = getName(object);
        return plural ? name.getPlural() : name.getSingular();
    }

    @Override
    public <T> void addNameConverter(NameConverter<T> converter) {
        if(converters.containsKey(converter.getType())) {
            logger.warn(String.format("Converter for class %s is already present, %s will be ignored", converter.getType(), converter));
            return;
        }
        converters.put(converter.getType(), converter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> NameConverter<? super T> getNameConverter(Class<T> type) {
        if(converters.containsKey(type)) {
            return (NameConverter<T>) converters.get(type);
        }
        if(Object.class.equals(type)) {
            return null;
        }
        Optional<NameConverter<?>> converter = Arrays
                .stream(type.getInterfaces())
                .filter(converters::containsKey)
                .findFirst()
                .map(converters::get);
        if(converter.isPresent()) {
            return (NameConverter<? super T>) converter.get();
        }
        return getNameConverter(type.getSuperclass());
    }

    @Override
    public void addNumberFormatter(NumberFormatter formatter) {
        if(formatters.containsKey(formatter)) {
            logger.warn(String.format("Formatter with handle %s is already present, %s will be ignored", formatter.getHandle(), formatter));
            return;
        }
        formatters.put(formatter.getHandle(), formatter);
    }

    @Override
    public NumberFormatter getNumberFormatter(Handle handle) {
        return formatters.get(handle);
    }

    @Override
    public NumberFormatter getNumberFormatter(TextConfiguration configuration) {
        return formatters.get(configuration.get(NUMBER_FORMATTER));
    }

    @Override
    public void addNumberSuffixFactory(NumberSuffixFactory factory) {
        if(factories.containsKey(factory)) {
            logger.warn(String.format("Factory with handle %s is already present, %s will be ignored", factory.getHandle(), factory));
            return;
        }
        if(factory.isInfinite() && defaultFactory == null) {
            defaultFactory = factory;
        }
        factories.put(factory.getHandle(), factory);
    }

    @Override
    public NumberSuffixFactory getNumberSuffixFactory(Handle handle) {
        return factories.get(handle);
    }

    @Override
    public NumberSuffixFactory getNumberSuffixFactory(TextConfiguration configuration) {
        return factories.getOrDefault(
                configuration.get(NUMBER_SUFFIX_FACTORY),
                getDefaultNumberSuffixFactory());
    }

    @Override
    public void setDefaultNumberSuffixFactory(NumberSuffixFactory factory) {
        if(!factory.isInfinite()) {
            logger.warn(String.format("Factory %s is not infinite and cannot be used as a default", factory));
            return;
        }
        defaultFactory = factory;
    }

    @Override
    public NumberSuffixFactory getDefaultNumberSuffixFactory() {
        return defaultFactory;
    }

    @Override
    public void setConfiguration(TextConfiguration configuration) {
        this.configuration.clear();
        this.configuration.set(configuration);
    }

    @Override
    public TextConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void setPluralConverter(PluralConverter converter) {
        pluralConverter = converter;
    }

    @Override
    public PluralConverter getPluralConverter() {
        return pluralConverter;
    }

    @Initialize
    private void initialize(TextConfiguration configuration) {
        setConfiguration(configuration);
    }
}
