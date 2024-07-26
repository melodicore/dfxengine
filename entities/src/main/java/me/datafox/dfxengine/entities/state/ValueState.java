package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.ValueDto;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueState implements DataState {
    private String typeId;
    private ValueDto value;

    @Override
    public String getHandle() {
        return value.getHandle();
    }
}
