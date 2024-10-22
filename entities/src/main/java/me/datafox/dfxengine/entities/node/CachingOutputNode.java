package me.datafox.dfxengine.entities.node;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.Node;

import java.util.List;

/**
 * @author datafox
 */
public abstract class CachingOutputNode implements Node {
    protected List<NodeData<?>> lastInputs;

    protected List<NodeData<?>> cachedOutputs;

    @Override
    public List<NodeData<?>> process(List<NodeData<?>> inputs, Context context) {
        if(cachedOutputs == null || !inputs.equals(lastInputs)) {
            lastInputs = inputs;
            cachedOutputs = calculateOutputs(inputs, context);
        }
        return cachedOutputs;
    }

    protected abstract List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context);
}
