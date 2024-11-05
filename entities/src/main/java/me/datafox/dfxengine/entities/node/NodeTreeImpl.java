package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.NodeTreeOwner;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;
import me.datafox.dfxengine.entities.exception.InvalidNodeTreeException;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.*;

/**
 * @author datafox
 */
@Data
public class NodeTreeImpl implements NodeTree {
    private final Handle handle;

    private final NodeTreeOwner owner;

    private final int order;

    private final List<Node> nodes;

    private final List<Node> nodesInternal;

    private final Set<NodeTreeAttribute> attributes;

    public NodeTreeImpl(String handle, NodeTreeOwner owner, int order, Collection<NodeTreeAttribute> attributes, Context context) {
        if(handle == null && attributes != null && attributes.contains(NodeTreeAttribute.CALLABLE)) {
            throw new InvalidNodeTreeException("NodeTree is marked as callable but does not have a Handle");
        }
        this.handle = context.getHandles().getTreeHandle(handle);
        this.owner = owner;
        this.order = order;
        nodesInternal = new ArrayList<>();
        nodes = Collections.unmodifiableList(nodesInternal);
        if(attributes == null || attributes.isEmpty()) {
            attributes = EnumSet.noneOf(NodeTreeAttribute.class);
        } else {
            attributes = EnumSet.copyOf(attributes);
        }
        this.attributes = Collections.unmodifiableSet((Set<NodeTreeAttribute>) attributes);
    }

    public void addNode(Node node) {
        nodesInternal.add(node);
    }
}
