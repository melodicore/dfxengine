package me.datafox.dfxengine.entities.definition.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ValueDto;
import me.datafox.dfxengine.entities.data.ValueMapData;
import me.datafox.dfxengine.entities.utils.EntityHandles;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapDataDefinition implements DataDefinition {
    private String handle;
    private String space;
    @Singular
    private List<ValueDto> values;

    @Override
    public String getTypeHandle() {
        return EntityHandles.getValueMapType().getId();
    }

    @Override
    public ValueMapData build(Engine engine) {
        return new ValueMapData(this);
    }
}
