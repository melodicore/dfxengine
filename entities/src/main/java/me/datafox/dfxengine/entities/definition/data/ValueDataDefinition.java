package me.datafox.dfxengine.entities.definition.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ValueData;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.values.ValueImpl;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueDataDefinition implements DataDefinition {
    private String handle;
    private String valueType;
    private String value;

    @Override
    public String getTypeHandle() {
        return EntityHandles.getValueType().getId();
    }

    @Override
    public ValueData build(Engine engine) {
        Handle h = EntityHandles.getData().getOrCreateHandle(handle);
        return new ValueData(new ValueImpl(
                h, EntityUtils.getNumeral(valueType, value), false));
    }
}
