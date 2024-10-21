package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;

import java.util.List;

/**
 * @author datafox
 */
public interface ConsumerNode extends Node {
    void consume(List<NodeData<?>> inputs, Context context);

    @Override
    default List<NodeOutput<?>> getOutputs() {
        return List.of();
    }

    @Override
    default List<NodeData<?>> process(List<NodeData<?>> inputs, Context context) {
        consume(inputs, context);
        return List.of();
    }
}
