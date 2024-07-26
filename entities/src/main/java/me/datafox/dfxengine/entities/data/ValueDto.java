package me.datafox.dfxengine.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueDto {
    public String handle;
    public NumeralType type;
    public String value;

    public static ValueDto of(Value value) {
        return of(value.getHandle().getId(), value.getBase());
    }

    public static ValueDto of(String handle, Numeral value) {
        return ValueDto
                .builder()
                .handle(handle)
                .type(value.getType())
                .value(value.getNumber().toString())
                .build();
    }

    public Value buildValue(Space space, boolean immutable) {
        return new ValueImpl(EntityUtils.getValueHandle(space, this), EntityUtils.getValueNumeral(this), immutable);
    }
}
