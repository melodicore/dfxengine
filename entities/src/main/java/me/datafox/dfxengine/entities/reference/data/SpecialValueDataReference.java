package me.datafox.dfxengine.entities.reference.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialValueDataReference implements Reference<Value> {
    public int index;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<Value> get(Engine engine) {
        return Stream.of(MappingOperationModifier.resultValue(index));
    }
}
