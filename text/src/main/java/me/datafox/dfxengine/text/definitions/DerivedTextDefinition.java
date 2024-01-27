package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.text.api.Name;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import static me.datafox.dfxengine.text.utils.internal.TextConstants.SINGULAR_DEFAULT;
import static me.datafox.dfxengine.text.utils.internal.TextConstants.SINGULAR_HANDLE_ID;

/**
 * @author datafox
 */
public class DerivedTextDefinition<T> implements TextDefinition {
    private final T object;

    public DerivedTextDefinition(T object) {
        this.object = object;
    }

    @Override
    public String getText(TextFactory factory, TextFactory.Context context) {
        Name<T> name = factory.getName(object);
        if(context.getById(SINGULAR_HANDLE_ID, SINGULAR_DEFAULT)) {
            return name.getSingular();
        } else {
            return name.getPlural();
        }
    }
}
