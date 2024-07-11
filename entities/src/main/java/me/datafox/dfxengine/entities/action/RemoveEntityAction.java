package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.definition.action.RemoveEntityActionDefinition;

import java.util.List;

/**
 * @author datafox
 */
public class RemoveEntityAction extends AbstractAction {
    public static final ActionParameters.Key<List<Entity>> ENTITIES = ActionParameters.key(List.of());

    private final RemoveEntityActionDefinition definition;

    public RemoveEntityAction(RemoveEntityActionDefinition definition, Engine engine) {
        super(definition.getHandle(), engine);
        this.definition = definition;
    }

    @Override
    public void run(ActionParameters parameters) {
        getEngine().removeMultiEntities(parameters.get(ENTITIES));
    }

    @Override
    public void link() {

    }

    @Override
    public void clear() {

    }
}
