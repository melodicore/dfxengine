package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.action.CreateEntityActionDefinition;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;

/**
 * @author datafox
 */
public class CreateEntityAction extends AbstractAction {
    public static final ActionParameters.Key<List<Handle>> HANDLES = ActionParameters.key("handles", List.of());

    private final CreateEntityActionDefinition definition;

    public CreateEntityAction(CreateEntityActionDefinition definition, Engine engine) {
        super(definition.getHandle(), engine);
        this.definition = definition;
    }

    @Override
    public void run(ActionParameters parameters) {
        getEngine().createMultiEntities(parameters.get(HANDLES));
    }

    @Override
    public void link() {
    }

    @Override
    public void clear() {
    }
}
