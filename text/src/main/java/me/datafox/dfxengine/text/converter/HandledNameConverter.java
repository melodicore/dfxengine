package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.NameConverter;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandledNameConverter implements NameConverter<Handled> {
    @Override
    public Class<Handled> getType() {
        return Handled.class;
    }

    @Override
    public boolean isPluralCapable() {
        return false;
    }

    @Override
    public String convert(Handled object) {
        return object.getHandle().getId();
    }

    @Override
    public String convertPlural(Handled object) {
        return null;
    }
}
