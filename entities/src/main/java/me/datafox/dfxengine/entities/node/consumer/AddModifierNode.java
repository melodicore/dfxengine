package me.datafox.dfxengine.entities.node.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.ConsumerNode;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.NodeInputImpl;
import me.datafox.dfxengine.entities.utils.TypeUtils;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.List;

/**
 * @author datafox
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddModifierNode implements ConsumerNode {
    private final NodeTree tree;

    private final List<NodeInput<?>> inputs;

    public AddModifierNode(NodeTree tree, DataType<?> modified, DataType<?> modifier) {
        this.tree = tree;
        inputs = List.of(
                new NodeInputImpl<>(this, modified),
                new NodeInputImpl<>(this, modifier));
    }

    @Override
    public void consume(List<NodeData<?>> inputs, Context context) {
        NodeData<?> modified = inputs.get(0);
        NodeData<?> modifier = inputs.get(1);
        List<Modifier> modifiers = TypeUtils.castToList(modifier, Modifier.class);
        if(Value.class.equals(modified.getType().getElementType())) {
            TypeUtils.castToList(modified, Value.class)
                    .forEach(v -> v.addModifiers(modifiers));
        } else {
            TypeUtils.castToList(modified, ValueMap.class)
                    .forEach(v -> v.addModifiers(modifiers));
        }
    }
}
