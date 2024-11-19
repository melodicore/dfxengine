package me.datafox.dfxengine.injector.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.exception.UnloadableClassException;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

import java.io.Serializable;

/**
 * A serializable class that represents a {@link Class} with type parameters.
 *
 * @param <T> type of the represented {@link Class}
 *
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ClassData<T> implements Serializable {
    private static final long serialVersionUID = 1000L;

    /**
     * A fully qualified class name ({@link Class#getName()}).
     */
    public String name;

    /**
     * A class signature that includes type parameters.
     */
    public String signature;

    private transient Class<T> type;

    /**
     * Public constructor for {@link ClassData}.
     *
     * @param type {@link Class} to be represented
     * @param signature signature of the class that includes type parameters
     */
    public ClassData(Class<T> type, String signature) {
        name = type.getName();
        this.signature = signature;
        this.type = type;
    }

    /**
     * Returns the {@link Class} that this object represents. The value is lazily loaded by using
     * {@link Class#forName(String)}.
     *
     * @return {@link Class} represented by this object
     * @throws UnloadableClassException if the {@link Class} cannot be loaded
     */
    public Class<T> getType() {
        if(type == null) {
            type = InjectorUtils.loadType(name);
        }
        return type;
    }
}
