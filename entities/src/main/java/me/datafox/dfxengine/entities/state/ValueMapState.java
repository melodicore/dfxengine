package me.datafox.dfxengine.entities.state;

import lombok.*;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.data.ValueDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ValueMapState implements DataState {
    public String handle;
    public String typeId;
    public List<ValueDto> values;

    @Builder
    public ValueMapState(String handle, String typeId, @Singular List<ValueDto> values) {
        this.handle = handle;
        this.typeId = typeId;
        this.values = new ArrayList<>(values);
    }
}
