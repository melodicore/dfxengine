package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * @author datafox
 */
public class ImmutableValueMapData extends AbstractData<ValueMap> {
    public ImmutableValueMapData(Handle handle, ValueMap data) {
        super(handle, EntityHandles.getValueType(), data);
        if(!data.isImmutable()) {
            throw new IllegalArgumentException("ImmutableValueMapData must have an immutable ValueMap");
        }
    }
}
