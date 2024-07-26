package me.datafox.dfxengine.entities.definition.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ValueData;
import me.datafox.dfxengine.entities.data.ValueDto;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueDataDefinition implements DataDefinition {
    public ValueDto value;

    @Override
    public String getHandle() {
        return value.getHandle();
    }

    @Override
    public ValueData build(Engine engine) {
        return new ValueData(this);
    }
}
