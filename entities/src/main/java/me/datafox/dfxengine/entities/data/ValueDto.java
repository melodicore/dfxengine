package me.datafox.dfxengine.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueDto {
    private String handle;
    private String valueType;
    private String value;

    public static ValueDto of(Value value) {
        return ValueDto
                .builder()
                .handle(value.getHandle().getId())
                .valueType(value.getBase().getType().name())
                .value(value.getBase().getNumber().toString())
                .build();
    }
}
