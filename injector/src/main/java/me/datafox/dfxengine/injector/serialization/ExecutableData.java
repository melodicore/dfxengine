package me.datafox.dfxengine.injector.serialization;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A serializable class that represents a {@link Constructor} or a {@link Method} with type parameters.
 *
 * @param <T> type of the {@link Class} that holds the represented {@link Constructor} or {@link Method}
 *
 * @author datafox
 */
@Data
@NoArgsConstructor
public final class ExecutableData<T> implements Serializable {
    private static final long serialVersionUID = 1000L;

    /**
     * Name of the {@link Method}, or {@code null} if this object represents a {@link Constructor}.
     */
    public String method;

    /**
     * Fully qualified name of the {@link Class} that contains this executable.
     */
    public String ownerName;

    /**
     * A {@link Constructor}/{@link Method} signature that includes type parameters.
     */
    public String signature;

    /**
     * A list of fully qualified names of the parameter {@link Class Classes} of this executable.
     */
    public ArrayList<String> parameterNames;

    public ArrayList<String> parameterSignatures;

    public HashMap<String, FieldData<?>> fields;

    public HashMap<String, ExecutableData<?>> methods;

    private transient Class<T> owner;

    private transient Class<?>[] parameters;

    public ExecutableData(Executable executable, Class<T> owner, String signature, List<String> parameterSignatures, List<FieldData<?>> fields, List<ExecutableData<?>> methods) {
        this.method = executable instanceof Method ? executable.getName() : null;
        ownerName = owner.getName();
        parameterNames = Arrays
                .stream(executable.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.toCollection(ArrayList::new));
        this.signature = signature;
        this.parameterSignatures = new ArrayList<>(parameterSignatures);
        this.fields = new HashMap<>(fields.stream().collect(Collectors.toMap(FieldData::getName, Function.identity())));
        this.methods = new HashMap<>(methods.stream().collect(Collectors.toMap(ExecutableData::getMethod, Function.identity())));
        this.owner = owner;
        parameters = executable.getParameterTypes();
    }

    public Executable getExecutable() {
        try {
            if(method == null) {
                return owner.getDeclaredConstructor(parameters);
            } else {
                return owner.getDeclaredMethod(method, parameters);
            }
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getOwner() {
        if(owner == null) {
            owner = InjectorUtils.loadType(ownerName);
        }
        return owner;
    }

    public Class<?>[] getParameters() {
        if(parameters == null) {
            parameters = parameterNames
                    .stream()
                    .map(InjectorUtils::loadType)
                    .toArray(Class[]::new);
        }
        return parameters;
    }
}