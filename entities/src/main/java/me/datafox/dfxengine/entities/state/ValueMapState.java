package me.datafox.dfxengine.entities.state;

import lombok.*;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.ValueDto;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapState implements DataState {
    private String handle;
    private String typeHandle;
    @Singular
    private List<ValueDto> values;
}
