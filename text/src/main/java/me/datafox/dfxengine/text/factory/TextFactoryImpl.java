package me.datafox.dfxengine.text.factory;

import lombok.Getter;
import me.datafox.dfxengine.collections.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.TEXT_FACTORY_NUMBER_FORMATTER_SPACE_ID;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class TextFactoryImpl implements TextFactory {
    private final Logger logger;

    private final HandleManager handleManager;

    @Getter
    private TextContext defaultContext;

    @Getter
    private final Space numberFormatterSpace;

    private final Map<Object,Name<?>> names;

    private final Map<Class<?>,NameConverter<?>> nameConverters;

    private final SortedSet<TextProcessor> textProcessors;

    @Getter
    private Function<String,String> pluralConverter;

    private final HandleMap<NumberFormatter> numberFormatters;

    @Inject
    public TextFactoryImpl(Logger logger, HandleManager handleManager) {
        this.logger = logger;
        this.handleManager = handleManager;
        defaultContext = createEmptyContext();
        numberFormatterSpace = handleManager.getOrCreateSpace(TEXT_FACTORY_NUMBER_FORMATTER_SPACE_ID);
        names = new HashMap<>();
        nameConverters = new HashMap<>();
        textProcessors = new TreeSet<>();
        pluralConverter = TextFactoryImpl::defaultPluralConverter;
        numberFormatters = new HashHandleMap<>(numberFormatterSpace);
    }

    @Override
    public String build(TextDefinition definition, TextContext context) {
        if(!defaultContext.isEmpty()) {
            if(context.isEmpty()) {
                context = defaultContext.copy();
            } else {
                context = defaultContext.copy().setAll(context, true);
            }
        }
        return definition.getText(this, context);
    }

    @Override
    public void setDefaultContext(TextContext context) {
        defaultContext = Optional
                .ofNullable(context)
                .orElse(createEmptyContext());
    }

    @Override
    public TextContext createEmptyContext() {
        return new TextContextImpl(this);
    }

    @Override
    public <T> void registerName(T object, String singular, String plural) {
        names.put(object, new Name<>(object, singular, plural));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> getName(T object) {
        if(names.containsKey(object)) {
            return (Name<T>) names.get(object);
        }

        String name = ((NameConverter<T>) getNameConverter(object.getClass())).getName(object);

        return new Name<>(object, name, pluralConverter.apply(name));
    }

    @Override
    public <T> void registerNameConverter(NameConverter<T> nameConverter) {
        nameConverters.put(nameConverter.getObjectClass(), nameConverter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> NameConverter<T> getNameConverter(Class<T> type) {
        return Optional
                .ofNullable((NameConverter<T>) nameConverters.get(type))
                .orElseGet(() -> defaultNameConverter(type));
    }

    @Override
    public void registerTextProcessor(TextProcessor processor) {
        textProcessors.add(processor);
    }

    @Override
    public Collection<TextProcessor> getTextProcessors() {
        return Collections.unmodifiableSortedSet(textProcessors);
    }

    @Override
    public void setPluralConverter(Function<String,String> pluralConverter) {
        this.pluralConverter = Objects.requireNonNullElseGet(
                pluralConverter,
                () -> TextFactoryImpl::defaultPluralConverter);
    }

    @Override
    public void registerNumberFormatter(NumberFormatter numberFormatter) {
        numberFormatters.putHandled(numberFormatter);
    }

    @Override
    public Optional<NumberFormatter> getNumberFormatter(Handle key) {
        return Optional.ofNullable(numberFormatters.get(key));
    }

    @SuppressWarnings("rawtypes")
    @Initialize
    private void initialize(List<NameConverter> nameConverters,
                            List<TextProcessor> textProcessors,
                            List<NumberFormatter> numberFormatters) {
        nameConverters.forEach(this::registerNameConverter);
        textProcessors.forEach(this::registerTextProcessor);
        numberFormatters.forEach(this::registerNumberFormatter);
    }

    private static <T> NameConverter<T> defaultNameConverter(Class<T> type) {
        if(Handled.class.isAssignableFrom(type)) {
            return new NameConverter<>() {
                @Override
                public String getName(T value) {
                    return ((Handled) value).getHandle().getId();
                }

                @Override
                public Class<T> getObjectClass() {
                    return type;
                }
            };
        } else if(Handle.class.isAssignableFrom(type)) {
            return new NameConverter<>() {
                @Override
                public String getName(T value) {
                    return ((Handle) value).getId();
                }

                @Override
                public Class<T> getObjectClass() {
                    return type;
                }
            };
        } else {
            return new NameConverter<>() {
                @Override
                public String getName(T value) {
                    return value.toString();
                }

                @Override
                public Class<T> getObjectClass() {
                    return type;
                }
            };
        }
    }

    private static String defaultPluralConverter(String str) {
        if(str.matches(".*([sx]|ch)$")) {
            return str + "es";
        }
        if(str.matches(".*[^aeiouy]y$")) {
            return str.substring(0, str.length() - 1) + "ies";
        }
        return str + "s";
    }
}
