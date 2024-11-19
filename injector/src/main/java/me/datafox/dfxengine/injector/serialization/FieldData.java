package me.datafox.dfxengine.injector.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * A serializable class that represents a {@link Field} with type parameters.
 *
 * @param <T> type of the {@link Class} that holds the represented {@link Field}
 *
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FieldData<T> implements Serializable {
    private static final long serialVersionUID = 1000L;

    public String name;

    public String typeName;

    public String signature;

    private transient Class<T> type;

    public FieldData(String name, Class<T> type, String signature) {
        this.name = name;
        typeName = type.getName();
        this.signature = signature;
        this.type = type;
    }

    public Class<T> getType() {
        if(type == null) {
            type = InjectorUtils.loadType(typeName);
        }
        return type;
    }
}
