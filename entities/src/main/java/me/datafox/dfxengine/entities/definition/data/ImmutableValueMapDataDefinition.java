package me.datafox.dfxengine.entities.definition.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.data.ImmutableValueMapData;
import me.datafox.dfxengine.entities.data.ValueDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ImmutableValueMapDataDefinition implements DataDefinition {
    public String handle;
    public String space;
    public List<ValueDto> values;

    @Builder
    public ImmutableValueMapDataDefinition(String handle, String space, @Singular List<ValueDto> values) {
        this.handle = handle;
        this.space = space;
        this.values = new ArrayList<>(values);
    }

    @Override
    public ImmutableValueMapData build(Engine engine) {
        return new ImmutableValueMapData(this);
    }
}
