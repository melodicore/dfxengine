package me.datafox.dfxengine.injector.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

/**
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FieldData<T> {
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
