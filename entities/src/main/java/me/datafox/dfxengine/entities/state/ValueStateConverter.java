package me.datafox.dfxengine.entities.state;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.state.StateConverter;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Getter
@Component(order = Integer.MAX_VALUE)
public class ValueStateConverter implements StateConverter<Value, ValueState> {
    private final SingleDataType<Value> type = SingleDataTypeImpl.of(Value.class);

    @Override
    public ValueState createState(EntityData<Value> data) {
        return new ValueState(data.getData());
    }
}
