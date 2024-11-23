package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.utils.NumeralUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueState implements DataState<Value> {
    public final SingleDataType<Value> type = SingleDataTypeImpl.of(Value.class);

    public String handle;

    public NumeralType valueType;

    public String value;

    public ValueState(Value value) {
        handle = value.getHandle().getId();
        valueType = value.getBase().getType();
        this.value = value.getBase().getNumber().toString();
    }

    @Override
    public void setState(Value data) {
        data.set(NumeralUtils.getNumeral(valueType, value));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("valueState", ValueState.class);
    }
}
