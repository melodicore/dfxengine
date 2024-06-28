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
     * @return {@inheritDoc}. Always returns {@link Handle Handle.class}
     */
    @Override
    public Class<Handle> getType() {
        return Handle.class;
    }

    /**
     * @return {@inheritDoc}. Always returns {@code false}
     */
    @Override
    public boolean isPluralCapable() {
        return false;
    }

    /**
     * @param object {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String convert(Handle object) {
        return Optional.ofNullable(object)
                .map(Handle::getId)
                .orElse("null");
    }

    /**
     * @param object ignored parameter
     * @return {@inheritDoc}. Always returns {@code null}
     */
    @Override
    public String convertPlural(Handle object) {
        return null;
    }
}
