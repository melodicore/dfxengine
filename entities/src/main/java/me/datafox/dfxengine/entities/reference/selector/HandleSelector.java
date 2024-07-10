package me.datafox.dfxengine.entities.reference.selector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.reference.Selector;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandleSelector implements Selector {
    private String handle;

    @Override
    public boolean isSingle(boolean isEntity) {
        if(!isEntity) {
            return true;
        }
        return EntityHandles.getEntities().getOrCreateHandle(handle).getTags().contains(EntityHandles.getSingleTag());
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return Stream.ofNullable(map.get(getHandle()));
    }
}
