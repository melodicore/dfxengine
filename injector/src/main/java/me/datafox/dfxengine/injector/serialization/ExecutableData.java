package me.datafox.dfxengine.injector.serialization;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public final class ExecutableData {
    public String method;

    public Class<?> owner;

    public String signature;

    public Class<?>[] parameters;

    public ArrayList<String> parameterSignatures;

    public HashMap<String, FieldData<?>> fields;

    public HashMap<String, ExecutableData> methods;

    public ExecutableData(Executable executable, Class<?> owner, String signature, List<String> parameterSignatures, List<FieldData<?>> fields, List<ExecutableData> methods) {
        this.method = executable instanceof Method ? executable.getName() : null;
        this.owner = owner;
        this.signature = signature;
        parameters = executable.getParameterTypes();
        this.parameterSignatures = new ArrayList<>(parameterSignatures);
        this.fields = new HashMap<>(fields.stream().collect(Collectors.toMap(FieldData::getName, Function.identity())));
        this.methods = new HashMap<>(methods.stream().collect(Collectors.toMap(ExecutableData::getMethod, Function.identity())));
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
}