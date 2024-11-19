package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.NameConverter;

import java.util.Optional;

/**
 * A {@link NameConverter} that uses {@link Handle#getId()} as the singular form. It is not capable of generating the
 * plural form on ts own.
 *
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandleNameConverter implements NameConverter<Handle> {
    /**
     * Returns the type of the object that this converter can convert. Always returns {@link Handle Handle.class}.
     *
     * @return type of the object that this converter can convert
     */
    @Override
    public Class<Handle> getType() {
        return Handle.class;
    }

    /**
     * Returns {@code true} if this converter can also convert to plural form. Always returns {@code false}.
     *
     * @return {@code false}, because this converter can also convert to plural form
     */
    @Override
    public boolean isPluralCapable() {
        return false;
    }

    /**
     * Converts an object to its given name in singular form.
     *
     * @param object object to be converted
     * @return name of the object in singular form
     */
    @Override
    public String convert(Handle object) {
        return Optional.ofNullable(object)
                .map(Handle::getId)
                .orElse("null");
    }

    /**
     * Converts an object to its given name in singular form, or returns {@code null} if this converter does not support
     * plural form. This converter does not support plural form.
     *
     * @param object object to be converted
     * @return {@code null}, because this converter does not support plural form
     */
    @Override
    public String convertPlural(Handle object) {
        return null;
    }
}
