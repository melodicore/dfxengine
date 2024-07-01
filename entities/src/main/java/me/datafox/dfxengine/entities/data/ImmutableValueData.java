package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ImmutableValueData extends AbstractData<Value> {
    public ImmutableValueData(Value data) {
        super(data.getHandle(), EntityHandles.getValueType(), data);
    }
}
