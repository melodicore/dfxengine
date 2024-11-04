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
public final class ClassData<T> {
    public Class<T> type;

    public String signature;

    public String getName() {
        return type.getName();
    }
}
