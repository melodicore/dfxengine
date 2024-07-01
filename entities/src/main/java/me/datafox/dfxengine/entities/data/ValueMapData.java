package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.state.ValueMapState;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class ValueMapData extends AbstractStatefulData<ValueMap> {
    public ValueMapData(Handle handle, ValueMap data) {
        super(handle, EntityHandles.getValueType(), data);
        if(data.isImmutable()) {
            throw new IllegalArgumentException("ValueMapEntityData must not have an immutable ValueMap");
        }
    }

    @Override
    public void setState(DataState state) {
        ValueMapState cast = castState(state, ValueMapState.class);
        Space space = getData().getSpace();
        getData().clear();
        getData().set(cast
                .getValues()
                .stream()
                .collect(Collectors.toMap(
                        value -> EntityUtils.getValueHandle(space, value),
                        EntityUtils::getValueNumeral)));
    }

    @Override
    public DataState getState() {
        return ValueMapState
                .builder()
                .handle(getHandle().getId())
                .typeHandle(getTypeHandle().getId())
                .values(getData()
                        .values()
                        .stream()
                        .map(ValueDto::of)
                        .collect(Collectors.toList()))
                .build();
    }

}
