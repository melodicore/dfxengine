package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.data.NodeDataImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
public class MultiListReferenceNode<T> implements SupplierNode {
    private final NodeTree tree;

    private final ListDataType<T> inputType;

    private final NodeOutput<List<T>> output;

    private final List<NodeOutput<List<T>>> outputs;

    private final Reference entity;

    private final Reference component;

    private final Reference data;

    public MultiListReferenceNode(NodeTree tree, ListDataType<T> type, Reference entity, Reference component, Reference data) {
        this.tree = tree;
        inputType = type;
        output = new NodeOutputImpl<>(this, type);
        outputs = List.of(output);
        this.entity = entity;
        this.component = component;
        this.data = data;
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        List<T> list = Stream.of(context.getEngine().getEntities())
                .flatMap(entity::get)
                .map(Entity::getComponents)
                .flatMap(component::get)
                .map(c -> c.getData(inputType))
                .flatMap(data::get)
                .map(EntityData::getData)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return List.of(new NodeDataImpl<>(inputType, list));
    }
}