package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.definition.data.ImmutableValueMapDataDefinition;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
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
public class ImmutableValueMapData extends AbstractData<ValueMap> {
    public ImmutableValueMapData(ImmutableValueMapDataDefinition definition) {
        super(definition.getHandle(), definition.getTypeHandle());
        setData(build(definition));
    }

    private ValueMap build(ImmutableValueMapDataDefinition definition) {
        Space s = EntityHandles.getHandleManager().getSpaces().get(definition.getSpace());
        Logger logger = LoggerFactory.getLogger(getClass());
        ValueMap map = new DelegatedValueMap(
                new TreeHandleMap<>(s, logger),
                true, logger);
        map.set(definition
                .getValues()
                .stream()
                .collect(Collectors.toMap(
                        value -> EntityUtils.getValueHandle(s, value),
                        EntityUtils::getValueNumeral)));
        return map;
    }
}
