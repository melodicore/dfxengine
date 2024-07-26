package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.definition.data.ValueDataDefinition;
import me.datafox.dfxengine.entities.state.ValueState;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ValueData extends AbstractStatefulData<Value> {
    public ValueData(ValueDataDefinition definition) {
        super(Value.class, definition.getHandle());
        setData(new ValueImpl(getHandle(),
                EntityUtils.getNumeral(definition.getValue().getType(), definition.getValue().getValue()),
                false));
    }

    @Override
    public void setState(DataState state) {
        ValueState cast = castState(state, ValueState.class);
        getData().set(EntityUtils.getNumeral(cast.getValue().getType(), cast.getValue().getValue()));
    }

    @Override
    public DataState getState() {
        return ValueState
                .builder()
                .typeId(getType().getName())
                .value(ValueDto.of(getData()))
                .build();
    }

}
