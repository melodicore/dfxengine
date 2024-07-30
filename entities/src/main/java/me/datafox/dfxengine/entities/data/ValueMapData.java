package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.definition.data.ValueMapDataDefinition;
import me.datafox.dfxengine.entities.state.ValueMapState;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.values.DelegatedValueMap;
import me.datafox.dfxengine.values.api.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class ValueMapData extends AbstractStatefulData<ValueMap> {
    public ValueMapData(ValueMapDataDefinition definition) {
        super(ValueMap.class, definition.getHandle());
        setData(build(definition));
    }

    private ValueMap build(ValueMapDataDefinition definition) {
        Space s = EntityHandles.getHandleManager().getSpaces().get(definition.getSpace());
        Logger logger = LoggerFactory.getLogger(getClass());
        ValueMap map = new DelegatedValueMap(
                new TreeHandleMap<>(s, logger),
                false, logger);
        definition.getValues()
                .stream()
                .map(v -> v.buildValue(s, false))
                .forEach(map::putHandled);
        return map;
    }

    @Override
    public void setState(DataState state) {
        ValueMapState cast = castState(state, ValueMapState.class);
        Space space = getData().getSpace();
        getData().clear();
        cast.getValues()
                .stream()
                .map(v -> v.buildValue(space, false))
                .forEach(getData()::putHandled);
    }

    @Override
    public DataState getState() {
        return ValueMapState
                .builder()
                .handle(getHandle().getId())
                .typeId(getType().getName())
                .values(getData()
                        .values()
                        .stream()
                        .map(ValueDto::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
