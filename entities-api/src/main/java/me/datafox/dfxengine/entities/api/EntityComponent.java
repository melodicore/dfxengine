package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

import java.util.List;

/**
 * @author datafox
 */
public interface EntityComponent extends Handled {
    Entity getEntity();

    <T> void putData(EntityData<T> data);

    <T> HandleMap<EntityData<T>> getData(DataType<T> type);

    List<NodeTree> getTrees();

    HandleMap<NodeTree> getCallableTrees();
}
