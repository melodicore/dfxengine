package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.definition.data.ImmutableValueDataDefinition;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ImmutableValueData extends AbstractData<Value> {
    public ImmutableValueData(ImmutableValueDataDefinition definition) {
        super(definition.getHandle(), definition.getTypeHandle());
        setData(new ValueImpl(getHandle(),
                EntityUtils.getNumeral(definition.getValueType(), definition.getValue()),
                true));
    }
}
