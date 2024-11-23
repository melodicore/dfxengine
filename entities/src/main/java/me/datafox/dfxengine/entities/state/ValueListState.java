package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.ListDataTypeImpl;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.utils.HandleUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.values.api.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ValueListState implements DataState<List<Value>> {
    public final ListDataType<Value> type = ListDataTypeImpl.of(Value.class);

    public String handle;

    public ArrayList<ValueState> values;

    @Builder
    public ValueListState(String handle, @Singular List<ValueState> values) {
        this.handle = handle;
        this.values = new ArrayList<>(values);
    }

    @Override
    public void setState(List<Value> data) {
        Map<String,Value> dataMap = data.stream()
                .collect(Collectors.toMap(
                        v -> HandleUtils.toIdWithSpace(v.getHandle()),
                        Function.identity()));
        values.forEach(state -> Optional
                .ofNullable(dataMap.get(state.getHandle()))
                .ifPresent(state::setState));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("valueListState", ValueListState.class);
    }
}
