package me.datafox.dfxengine.entities.definition.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ValueDto;
import me.datafox.dfxengine.entities.data.ValueMapData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ValueMapDataDefinition implements DataDefinition {
    public String handle;
    public String space;
    public List<ValueDto> values;

    @Builder
    public ValueMapDataDefinition(String handle, String space, @Singular List<ValueDto> values) {
        this.handle = handle;
        this.space = space;
        this.values = new ArrayList<>(values);
    }

    @Override
    public ValueMapData build(Engine engine) {
        return new ValueMapData(this);
    }
}
