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

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImmutableValueMapDataDefinition implements DataDefinition {
    private String handle;
    private String space;
    private List<ValueDto> values;

    @Override
    public String getTypeHandle() {
        return EntityHandles.getValueMapType().getId();
    }

    @Override
    public ImmutableValueMapData build(Engine engine) {
        return new ImmutableValueMapData(this);
    }
}
