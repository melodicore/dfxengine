package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.state.ValueState;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ValueData extends AbstractStatefulData<Value> {
    public ValueData(Value data) {
        super(data.getHandle(), EntityHandles.getValueType(), data);
        if(data.isImmutable()) {
            throw new IllegalArgumentException("ValueData must not have an immutable Value");
        }
    }

    @Override
    public void setState(DataState state) {
        ValueState cast = castState(state, ValueState.class);
        getData().set(EntityUtils.getNumeral(cast.getValueType(), cast.getValue()));
    }

    @Override
    public DataState getState() {
        return ValueState
                .builder()
                .handle(getHandle().getId())
                .typeHandle(getTypeHandle().getId())
                .valueType(getData().getBase().getType().name())
                .value(getData().getBase().getNumber().toString())
                .build();
    }

}
