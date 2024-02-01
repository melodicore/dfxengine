package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.text.api.Name;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.SINGULAR;

/**
 * @author datafox
 */
public class NamedTextDefinition<T> extends AbstractTextDefinition {
    private final T object;

    public NamedTextDefinition(T object) {
        this.object = object;
    }

    @Override
    protected String getTextInternal(TextFactory factory, TextContext context) {
        Name<T> name = factory.getName(object);
        if(context.get(SINGULAR)) {
            return name.getSingular();
        } else {
            return name.getPlural();
        }
    }
}
