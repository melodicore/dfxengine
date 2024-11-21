package me.datafox.dfxengine.entities.entity;

import lombok.Data;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.*;

/**
 * @author datafox
 */
@Data
public class EntityComponentImpl implements EntityComponent {
    private final Handle handle;

    private final Entity entity;

    private final Map<DataType<?>, HandleMap<EntityData<?>>> data;

    private final List<NodeTree> trees;

    private final List<NodeTree> treesInternal;

    private final HandleMap<NodeTree> callableTrees;

    private final HandleMap<NodeTree> callableTreesInternal;

    private final Context context;

    public EntityComponentImpl(String handle, Entity entity, Context context) {
        this.handle = context.getHandles().getEntityHandle(handle);
        this.entity = entity;
        data = new HashMap<>();
        treesInternal = new ArrayList<>();
        trees = Collections.unmodifiableList(treesInternal);
        callableTreesInternal = new HashHandleMap<>(context.getHandles().getTreeSpace());
        callableTrees = callableTreesInternal.unmodifiable();
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void putData(EntityData<T> datum) {
        HandleMap<EntityData<T>> map;
        if(data.containsKey(datum.getType())) {
            map = (HandleMap<EntityData<T>>) (HandleMap<?>) data.get(datum.getType());
        } else {
            map = new HashHandleMap<>(context.getHandles().getDataSpace());
            data.put(datum.getType(), (HandleMap<EntityData<?>>) (HandleMap<?>) map);
        }
        map.putHandled(datum);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> HandleMap<EntityData<T>> getData(DataType<T> type) {
        return (HandleMap<EntityData<T>>) (HandleMap<?>) data.get(type);
    }

    public void clearData() {
        data.clear();
    }

    public void addTree(NodeTree tree) {
        treesInternal.add(tree);
        if(tree.getAttributes().contains(NodeTreeAttribute.CALLABLE)) {
            callableTreesInternal.put(tree.getHandle(), tree);
        }
    }
}
