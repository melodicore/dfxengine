package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.state.DataState;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueState implements DataState {
    private String handle;
    private String typeHandle;
    private String valueType;
    private String value;
}
