package me.datafox.dfxengine.entities;

import lombok.Data;
import me.datafox.dfxengine.entities.api.EntitySystem;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.math.api.Numeral;

import java.util.*;

/**
 * @author datafox
 */
@Data
public class EntitySystemImpl implements EntitySystem {
    private final Numeral interval;

    private final List<NodeTree> trees;

    private final List<NodeTree> treesInternal;

    public EntitySystemImpl(Numeral interval) {
        this.interval = interval;
        treesInternal = new ArrayList<>();
        trees = Collections.unmodifiableList(treesInternal);
    }

    public void addTree(NodeTree tree) {
        treesInternal.add(tree);
    }
}
