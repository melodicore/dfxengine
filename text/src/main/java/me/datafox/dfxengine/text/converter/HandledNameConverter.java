package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.NameConverter;

import java.util.Optional;

/**
 * A {@link NameConverter} that uses {@link Handled#getHandle()} followed by {@link Handle#getId()} as the singular
 * form. It is not capable of generating the plural form on its own.
 *
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandledNameConverter implements NameConverter<Handled> {
    /**
     * @return {@inheritDoc}. Always returns {@link Handled Handled.class}
     */
    @Override
    public Class<Handled> getType() {
        return Handled.class;
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
    public String convert(Handled object) {
        return Optional.ofNullable(object)
                .map(Handled::getHandle)
                .map(Handle::getId)
                .orElse("null");
    }

    /**
     * @param object ignored parameter
     * @return {@inheritDoc}. Always returns {@code null}
     */
    @Override
    public String convertPlural(Handled object) {
        return null;
    }
}
