package me.datafox.dfxengine.entities.reference.data;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public abstract class SelectorDataReference<T> implements Reference<T> {
    @Override
    public boolean isSingle() {
        return getComponent().isSingle() && getSelector().isSingle(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<T> get(Engine engine) {
        return EntityUtils.flatMapEntityData(engine,
                        getComponent(),
                        getTypeHandle(),
                        map -> getSelector().select(map, engine))
                .map(d -> (T) d.getData());
    }

    protected abstract Reference<EntityComponent> getComponent();

    protected abstract String getTypeHandle();

    protected abstract Selector getSelector();
}
