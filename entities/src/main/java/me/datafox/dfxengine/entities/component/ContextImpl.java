package me.datafox.dfxengine.entities.component;

import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.entities.api.component.*;
import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;
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
@Component(order = Integer.MAX_VALUE)
@Getter
public class ContextImpl implements Context {
    private Injector injector;

    private Logger logger;

    private ConfigurationManager configurationManager;

    private HandleManager handleManager;

    private EntityHandles handles;

    private Engine engine;

    private EntityFactory entityFactory;

    private NodeFactory nodeFactory;

    private NodeResolver nodeResolver;

    private SerializationHandler<?, ?> serializer;

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
                           ConfigurationManager configurationManager,
                           HandleManager handleManager,
                           EntityHandles handles,
                           Engine engine,
                           EntityFactory entityFactory,
                           NodeFactory nodeFactory,
                           NodeResolver nodeResolver,
                           SerializationHandler<?,?> serializer) {
        this.injector = injector;
        this.logger = logger;
        this.configurationManager = configurationManager;
        this.handleManager = handleManager;
        this.handles = handles;
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.nodeFactory = nodeFactory;
        this.nodeResolver = nodeResolver;
        this.serializer = serializer;
    }
}
