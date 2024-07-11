package me.datafox.dfxengine.entities.api;

/**
 * @author datafox
 */
public interface EntityListener {
    void added(Entity entity);

    void removed(Entity entity);
}
