package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;

import java.util.List;

/**
 * @author datafox
 */
public interface SupplierNode extends Node {
    List<NodeData<?>> supply(Context context);

    @Override
    default List<NodeInput<?>> getInputs() {
        return List.of();
    }

    @Override
    default List<NodeData<?>> process(List<NodeData<?>> inputs, Context context) {
        return supply(context);
    }
}
