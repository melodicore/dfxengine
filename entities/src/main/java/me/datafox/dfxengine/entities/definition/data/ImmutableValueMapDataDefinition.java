package me.datafox.dfxengine.entities.definition.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ImmutableValueMapData;
import me.datafox.dfxengine.entities.data.ValueDto;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.values.DelegatedValueMap;
import me.datafox.dfxengine.values.api.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImmutableValueMapDataDefinition implements DataDefinition {
    private String handle;
    private final String typeHandle = EntityHandles.getValueType().getId();
    private String space;
    private List<ValueDto> values;

    @Override
    public ImmutableValueMapData build(Engine engine) {
        Handle h = EntityHandles.getData().getOrCreateHandle(getHandle());
        Space s = EntityHandles.getHandleManager().getSpaces().get(getSpace());
        Logger logger = LoggerFactory.getLogger(ImmutableValueMapData.class);
        ValueMap map = new DelegatedValueMap(
                new TreeHandleMap<>(s, logger),
                true, logger);
        map.set(getValues()
                .stream()
                .collect(Collectors.toMap(
                        value -> EntityUtils.getValueHandle(s, value),
                        EntityUtils::getValueNumeral)));
        return new ImmutableValueMapData(h, map);
    }
}
