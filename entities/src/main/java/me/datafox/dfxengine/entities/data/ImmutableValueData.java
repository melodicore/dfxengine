package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.definition.data.ImmutableValueDataDefinition;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ImmutableValueData extends AbstractData<Value> {
    public ImmutableValueData(ImmutableValueDataDefinition definition) {
        super(Value.class, definition.getHandle());
        getHandle().getTags().add(EntityHandles.getImmutableTag());
        setData(new ValueImpl(getHandle(),
                EntityUtils.getNumeral(definition.getValue().getType(), definition.getValue().getValue()),
                true));
    }
}
