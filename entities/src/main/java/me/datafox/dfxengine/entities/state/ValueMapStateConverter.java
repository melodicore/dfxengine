package me.datafox.dfxengine.entities.state;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.state.StateConverter;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Getter
@Component(order = Integer.MAX_VALUE)
public class ValueMapStateConverter implements StateConverter<ValueMap, ValueMapState> {
    private final SingleDataType<ValueMap> type = SingleDataTypeImpl.of(ValueMap.class);

    @Override
    public ValueMapState createState(EntityData<ValueMap> data) {
        return new ValueMapState(data.getHandle().getId(),
                data.getData()
                        .values()
                        .stream()
                        .map(ValueState::new)
                        .collect(Collectors.toList()));
    }
}
