package me.datafox.dfxengine.entities.api;

/**
 * @author datafox
 */
public interface EntitySystem extends Comparable<EntitySystem> {
    int getPriority();

    void update(Engine engine, float delta);

    @Override
    default int compareTo(EntitySystem o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
