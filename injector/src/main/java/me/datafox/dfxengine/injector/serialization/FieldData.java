package me.datafox.dfxengine.injector.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FieldData<T> {
    public String name;

    public Class<T> type;

    public String signature;
}
