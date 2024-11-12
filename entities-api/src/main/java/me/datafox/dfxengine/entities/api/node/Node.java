package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;

import java.util.List;

/**
 * @author datafox
 */
public interface Node {
    NodeTree getTree();

    List<NodeInput<?>> getInputs();

    List<NodeOutput<?>> getOutputs();

    List<NodeData<?>> process(List<NodeData<?>> inputs, Context context);
}
