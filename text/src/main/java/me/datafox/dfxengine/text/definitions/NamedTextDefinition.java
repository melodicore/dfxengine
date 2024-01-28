package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.Name;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;

/**
 * @author datafox
 */
public class NamedTextDefinition<T> implements TextDefinition {
    private final T object;

    public NamedTextDefinition(T object) {
        this.object = object;
    }

    @Override
    public String getText(TextFactory factory, TextContext context) {
        Name<T> name = factory.getName(object);
        if(context.get(SINGULAR)) {
            return name.getSingular();
        } else {
            return name.getPlural();
        }
    }
}
