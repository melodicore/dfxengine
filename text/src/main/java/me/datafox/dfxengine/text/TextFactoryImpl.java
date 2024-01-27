package me.datafox.dfxengine.text;

import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.collections.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.Name;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.utils.StringUtils;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;

import static me.datafox.dfxengine.text.utils.internal.TextConstants.*;

/**
 * @author datafox
 */
@Component(defaultFor = TextFactory.class)
public class TextFactoryImpl implements TextFactory {
    private final Logger logger;

    private final HandleManager handleManager;

    @Getter
    @Setter
    private Context defaultContext;

    @Getter
    private final Space contextSpace;

    @Getter
    private final Space numberFormatterSpace;

    private final Map<Object,Name<?>> names;

    @Getter
    private Function<String,String> pluralConverter;

    private final HandleMap<NumberFormatter> numberFormatters;

    @Inject
    public TextFactoryImpl(Logger logger, HandleManager handleManager) {
        this.logger = logger;
        this.handleManager = handleManager;
        defaultContext = createEmptyContext();
        contextSpace = handleManager.getOrCreateSpace(TEXT_FACTORY_CONTEXT_SPACE_ID);
        numberFormatterSpace = handleManager.getOrCreateSpace(TEXT_FACTORY_NUMBER_FORMATTER_SPACE_ID);
        names = new HashMap<>();
        pluralConverter = TextFactoryImpl::defaultPluralConverter;
        numberFormatters = new HashHandleMap<>(numberFormatterSpace);
    }

    @Override
    public String build(Context context, TextDefinition definition) {
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
    public Context createEmptyContext() {
        return new ContextImpl();
    }

    @Override
    public <T> void registerName(T object, String singular, String plural) {
        names.put(object, new NameImpl<>(object, singular, plural));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Name<T> getName(T object) {
        if(names.containsKey(object)) {
            return (Name<T>) names.get(object);
        }

        String singular;

        if(object instanceof Handled) {
            singular = StringUtils.capitalize(((Handled) object).getHandle().getId());
        } else {
            singular = object.toString();
        }

        return new NameImpl<>(object, singular, pluralConverter.apply(singular));
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

    private static String defaultPluralConverter(String str) {
        if(str.matches(".*([sx]|ch)$")) {
            return str + "es";
        }
        if(str.matches(".*[^aeiouy]y$")) {
            return str.substring(0, str.length() - 1) + "ies";
        }
        return str + "s";
    }

    public class ContextImpl implements Context {
        private final HandleMap<String> params;

        private ContextImpl() {
            params = new HashHandleMap<>(contextSpace);
        }

        @Override
        public TextFactory getFactory() {
            return TextFactoryImpl.this;
        }

        @Override
        public Space getSpace() {
            return contextSpace;
        }

        @Override
        public Context copy() {
            return new ContextImpl().setAll(this, true);
        }

        @Override
        public Map<Handle,String> getAll() {
            return Collections.unmodifiableMap(params);
        }

        @Override
        public Context setAll(Context other, boolean overwrite) {
            if(overwrite) {
                params.putAll(other.getAll());
            } else {
                other.getAll().forEach(params::putIfAbsent);
            }
            return this;
        }

        @Override
        public String get(Handle key, String defaultValue) {
            return params.getOrDefault(key, defaultValue);
        }

        @Override
        public boolean get(Handle key, boolean defaultValue) {
            String param = params.get(key);
            if("true".equalsIgnoreCase(param)) {
                return true;
            }
            if("false".equalsIgnoreCase(param)) {
                return false;
            }
            return defaultValue;
        }

        @Override
        public int get(Handle key, int defaultValue) {
            try {
                return Integer.parseInt(params.get(key));
            } catch(NumberFormatException e) {
                return defaultValue;
            }
        }

        @Override
        public Context set(Handle key, String value) {
            params.put(key, value);
            return this;
        }

        @Override
        public Context set(Handle key, boolean value) {
            params.put(key, String.valueOf(value));
            return this;

        }

        @Override
        public Context set(Handle key, int value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        @Override
        public boolean isEmpty() {
            return params.isEmpty();
        }
    }
}
