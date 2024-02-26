package me.datafox.dfxengine.injector;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.typeParameterMismatch;

/**
 * Represents a type. This class is used when requesting components by generic superclasses or -interfaces.
 *
 * @author datafox
 */
@Builder
@Data
public class TypeRef<T> {
    private final Class<T> type;

    @Singular
    private final List<TypeRef<?>> parameters;

    public TypeRef(Class<T> type, List<TypeRef<?>> parameters) {
        if((!Array.class.equals(type) || parameters.size() != 1) &&
                type.getTypeParameters().length != parameters.size()) {
            throw LogUtils.logExceptionAndGet(LoggerFactory.getLogger(TypeRef.class),
                    typeParameterMismatch(type, type.getTypeParameters().length, parameters.size()),
                    IllegalArgumentException::new);
        }
        this.type = type;
        this.parameters = parameters;
    }


    public static TypeRef<Object> object() {
        return builder()
                .type(Object.class)
                .build();
    }

    public boolean isAssignableFrom(TypeRef<?> other) {
        if(parameters.size() != other.parameters.size() || !type.isAssignableFrom(other.type)) {
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
                        .map(TypeRef::toString)
                        .collect(Collectors.joining(", ")) + ">"));
    }

    public static <T> TypeRef<T> of(Class<T> type, List<TypeRef<?>> parameters) {
        return TypeRef
                .<T>builder()
                .type(type)
                .parameters(parameters)
                .build();
    }

    public static <T> TypeRef<T> of(Class<T> type, TypeRef<?>... parameters) {
        return of(type, Arrays.asList(parameters));
    }
}
