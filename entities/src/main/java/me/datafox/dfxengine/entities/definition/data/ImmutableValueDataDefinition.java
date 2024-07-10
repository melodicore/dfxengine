package me.datafox.dfxengine.entities.definition.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ImmutableValueData;
import me.datafox.dfxengine.entities.utils.EntityHandles;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImmutableValueDataDefinition implements DataDefinition {
    private String handle;
    private String valueType;
    private String value;

    @Override
    public String getTypeHandle() {
        return EntityHandles.getValueType().getId();
    }

    @Override
    public ImmutableValueData build(Engine engine) {
        return new ImmutableValueData(this);
    }
}
