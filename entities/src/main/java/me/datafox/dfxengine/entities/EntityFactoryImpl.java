package me.datafox.dfxengine.entities;

import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class EntityFactoryImpl implements EntityFactory {
    @Inject
    private Context context;

    @Inject
    private NodeFactory nodeFactory;

    @Override
    public Entity buildEntity(EntityDefinition definition) {
        EntityImpl entity = new EntityImpl(definition.getHandle(), context);

        definition.getComponents()
                .stream()
                .map(component -> buildComponent(entity, component))
                .forEach(entity::addComponent);

        return entity;
    }

    private EntityComponent buildComponent(Entity entity, ComponentDefinition definition) {
        EntityComponentImpl component = new EntityComponentImpl(definition.getHandle(), entity, context);

        definition.getTrees()
                .stream()
                .map(tree -> nodeFactory.buildTree(component, tree))
                .forEach(component::addTree);

        return component;
    }
}
