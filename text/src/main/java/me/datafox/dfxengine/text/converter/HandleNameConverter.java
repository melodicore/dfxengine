package me.datafox.dfxengine.text.converter;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.NameConverter;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandleNameConverter implements NameConverter<Handle> {
    @Override
    public Class<Handle> getType() {
        return Handle.class;
    }

    @Override
    public boolean isPluralCapable() {
        return false;
    }

    @Override
    public String convert(Handle object) {
        return object.getId();
    }

    @Override
    public String convertPlural(Handle object) {
        return null;
    }
}
