package me.datafox.dfxengine.injector.serialization;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    public String ownerName;

    public String signature;

    public ArrayList<String> parameterNames;

    public ArrayList<String> parameterSignatures;

    public HashMap<String, FieldData<?>> fields;

    public HashMap<String, ExecutableData> methods;

    private transient Class<?> owner;

    private transient Class<?>[] parameters;

    public ExecutableData(Executable executable, Class<?> owner, String signature, List<String> parameterSignatures, List<FieldData<?>> fields, List<ExecutableData> methods) {
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

    public Class<?> getOwner() {
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