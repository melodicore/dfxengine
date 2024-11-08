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
public final class ClassData<T> {
    public String name;

    public String signature;

    private transient Class<T> type;

    public ClassData(Class<T> type, String signature) {
        name = type.getName();
        this.signature = signature;
        this.type = type;
    }

    public Class<T> getType() {
        if(type == null) {
            type = InjectorUtils.loadType(name);
        }
        return type;
    }
}
