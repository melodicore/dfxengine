package me.datafox.dfxengine.entities.api.component;

import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeGroup;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.math.api.Numeral;
import org.slf4j.Logger;

import java.util.Collection;

/**
 * @author datafox
 */
public interface Context {
    Injector getInjector();

    HandleManager getHandleManager();

    EntityHandles getHandles();

    Engine getEngine();

    EntityFactory getEntityFactory();

    NodeFactory getNodeFactory();

    NodeResolver getNodeResolver();

    Logger getLogger();

    Entity getCurrentEntity();

    void setCurrentEntity(Entity entity);

    EntitySystem getCurrentSystem();

    void setCurrentSystem(EntitySystem system);

    EntityComponent getCurrentComponent();

    void setCurrentComponent(EntityComponent component);

    NodeTree getCurrentTree();

    void setCurrentTree(NodeTree tree);

    NodeGroup getCurrentGroup();

    void setCurrentGroup(NodeGroup group);

    Node getCurrentNode();

    void setCurrentNode(Node node);

    default <T> void invokeEvent(T event) {
        getInjector().invokeEvent(event);
    }

    default void invokeEvents(Collection<?> events) {
        getInjector().invokeEvents(events);
    }

    default Numeral getDelta() {
        return getEngine().getDelta();
    }
}
