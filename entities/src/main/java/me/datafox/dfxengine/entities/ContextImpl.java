package me.datafox.dfxengine.entities;

import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeGroup;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import org.slf4j.Logger;

/**
 * @author datafox
 */
@Component
@Getter
public class ContextImpl implements Context {
    private Injector injector;

    private Logger logger;

    private HandleManager handleManager;

    private EntityHandles handles;

    private Engine engine;

    private EntityFactory entityFactory;

    private NodeFactory nodeFactory;

    private NodeResolver nodeResolver;

    @Setter
    private Entity currentEntity;

    @Setter
    private EntitySystem currentSystem;

    @Setter
    private EntityComponent currentComponent;

    @Setter
    private NodeTree currentTree;

    @Setter
    private NodeGroup currentGroup;

    @Setter
    private Node currentNode;

    @Initialize
    public void initialize(Injector injector,
                           Logger logger,
                           HandleManager handleManager,
                           EntityHandles handles,
                           Engine engine,
                           EntityFactory entityFactory,
                           NodeFactory nodeFactory,
                           NodeResolver nodeResolver) {
        this.injector = injector;
        this.logger = logger;
        this.handleManager = handleManager;
        this.handles = handles;
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.nodeFactory = nodeFactory;
        this.nodeResolver = nodeResolver;
    }
}
