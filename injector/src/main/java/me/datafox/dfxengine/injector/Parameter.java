package me.datafox.dfxengine.injector;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Builder
@Data
public class Parameter<T> {
    private final Class<T> type;

    @Singular
    private final List<Parameter<?>> parameters;

    public static Parameter<Object> object() {
        return builder()
                .type(Object.class)
                .build();
    }

    public boolean isAssignableFrom(Parameter<?> other) {
        if(!type.isAssignableFrom(other.type)) {
            return false;
        }
        for(int i = 0; i < parameters.size(); i++) {
            if(!parameters.get(i).isAssignableFrom(other.parameters.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(Parameter::toString)
                        .collect(Collectors.joining(", ")) + ">"));
    }

    public static List<Parameter<?>> listOf(Class<?> type, List<Parameter<?>> parameters) {
        return List.of(of(type, parameters));
    }

    public static List<Parameter<?>> listOf(Class<?> type, Parameter<?> ... parameters) {
        return List.of(of(type, parameters));
    }

    public static <T> Parameter<T> of(Class<T> type, List<Parameter<?>> parameters) {
        return Parameter
                .<T>builder()
                .type(type)
                .parameters(parameters)
                .build();
    }

    public static <T> Parameter<T> of(Class<T> type, Parameter<?> ... parameters) {
        return of(type, Arrays.asList(parameters));
    }
}
