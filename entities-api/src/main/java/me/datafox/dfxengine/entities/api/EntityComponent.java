package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityComponent extends Handled, NodeTreeOwner {
    Entity getEntity();

    <T> void putData(EntityData<T> data);

    <T> HandleMap<EntityData<T>> getData(DataType<T> type);

    HandleMap<NodeTree> getCallableTrees();
}
