package me.datafox.dfxengine.entities.reference.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaticValueDataReference implements Reference<Value> {
    private String valueType;
    private String value;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<Value> get(Engine engine) {
        return Stream.of(new StaticValue(EntityUtils.getNumeral(valueType, value)));
    }
}
