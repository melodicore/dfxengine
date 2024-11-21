package me.datafox.dfxengine.entities.state;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.state.StateConverter;
import me.datafox.dfxengine.entities.data.ListDataTypeImpl;
import me.datafox.dfxengine.entities.utils.HandleUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.values.api.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Getter
@Component(order = Integer.MAX_VALUE)
public class ValueListStateConverter implements StateConverter<List<Value>, ValueListState> {
    private final ListDataType<Value> type = ListDataTypeImpl.of(Value.class);

    @Override
    public ValueListState createState(EntityData<List<Value>> data) {
        return new ValueListState(data.getHandle().getId(),
                data.getData().stream()
                        .map(value -> new ValueState(
                                HandleUtils.toIdWithSpace(value.getHandle()),
                                value.getBase().getType(),
                                value.getBase().getNumber().toString()))
                        .collect(Collectors.toList()));
    }
}
