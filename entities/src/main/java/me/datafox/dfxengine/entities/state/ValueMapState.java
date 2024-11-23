package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.utils.NumeralUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ValueMapState implements DataState<ValueMap> {
    public final SingleDataType<ValueMap> type = SingleDataTypeImpl.of(ValueMap.class);

    public String handle;

    public ArrayList<ValueState> values;

    @Builder
    public ValueMapState(String handle, @Singular List<ValueState> values) {
        this.handle = handle;
        this.values = new ArrayList<>(values);
    }

    @Override
    public void setState(ValueMap data) {
        values.forEach(state -> setState(state, data));
    }

    private void setState(ValueState state, ValueMap data) {
        if(!data.containsKey(state.getHandle())) {
            return;
        }
        data.get(state.getHandle()).set(NumeralUtils.getNumeral(state.getValueType(), state.getValue()));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("valueMapState", ValueMapState.class);
    }
}
