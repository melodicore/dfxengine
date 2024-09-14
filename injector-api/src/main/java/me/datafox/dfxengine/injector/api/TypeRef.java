package me.datafox.dfxengine.injector.api;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a type with parameters. This class is used when requesting {@link Component Components} from the
 * {@link Injector}. As an example, if you wanted to reference {@code Component<Type1,Type2<Type3>>}, you would call
 * {@code TypeRef.of(Component.class, TypeRef.of(Type1.class), TypeRef.of(Type2.class, TypeRef.of(Type3.class)));}. The
 * constructor checks for parameter count and throws an exception if an invalid amount of type parameters.
 *
 * @author datafox
 */
@Builder
@Data
public final class TypeRef<T> {
    private final Class<T> type;

    @Singular
    private final List<TypeRef<?>> parameters;

    /**
     * Constructs a type reference
     * @param type type to be represented
     * @param parameters type parameters to be represented
     *
     * @throws ParameterCountMismatchException if the amount of parameters for the specified type is different to the
     * amount of provided parameters
     */
    public TypeRef(Class<T> type, List<TypeRef<?>> parameters) {
        if(type.getTypeParameters().length != parameters.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Class ").append(type.getName()).append(" has ").append(type.getTypeParameters().length).append(" type parameter");
            if(type.getTypeParameters().length != 1) {
                sb.append("s");
            }
            sb.append(" but ").append(parameters.size());
            if(parameters.size() == 1) {
                sb.append(" was");
            } else {
                sb.append(" were");
            }
            sb.append(" provided");
            throw new ParameterCountMismatchException(sb.toString());
        }
        this.type = type;
        this.parameters = parameters;
    }

    /**
     * The purpose of this method is to be able to cast a type reference to a parameterised type with multiple type
     * layers without the compiler complaining about illegal casting. For example, calling {@code TypeRef.of(Map.class,
     * String.class, Integer.class)} will return an object with type {@code TypeRef<Map>} without the parameters for
     * {@code Map}, and casting that directly to {@code TypeRef<Map<String,Integer>>} is not allowed. But casting from
     * {@code TypeRef<?>} is allowed, so calling {@code (TypeRef<Map<String,Integer>>) TypeRef.of(Map.class,
     * String.class, Integer.class).uncast()} will work. This is mainly useful for getting the component with the right
     * runtime type with {@link Injector#getComponent(TypeRef)} and related methods.
     *
     * @return this type reference as {@code TypeRef<?>}
     */
    public TypeRef<?> uncast() {
        return this;
    }

    /**
     * @return String representation of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     * without packages
     */
    public String toStringWithoutPackage() {
        return type.getSimpleName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(TypeRef::toStringWithoutPackage)
                        .collect(Collectors.joining(",")) + ">"));
    }

    /**
     * @return String representation of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     * with the parameters without packages
     */
    public String toStringParametersWithoutPackage() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(TypeRef::toStringWithoutPackage)
                        .collect(Collectors.joining(",")) + ">"));
    }

    /**
     * @return String representation of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     */
    @Override
    public String toString() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(TypeRef::toString)
                        .collect(Collectors.joining(",")) + ">"));
    }

    /**
     * @return type reference for {@link Object}
     */
    public static TypeRef<Object> object() {
        return builder()
                .type(Object.class)
                .build();
    }

    /**
     * @param type type for the type reference
     * @param parameters parameters for the type reference
     * @return type reference for the specified parameters
     * @param <T> type for the type reference
     */
    public static <T> TypeRef<T> of(Class<T> type, List<TypeRef<?>> parameters) {
        return TypeRef
                .<T>builder()
                .type(type)
                .parameters(parameters)
                .build();
    }

    /**
     * @param type type for the type reference
     * @param parameters parameters for the type reference
     * @return type reference for the specified parameters
     * @param <T> type for the type reference
     */
    public static <T> TypeRef<T> of(Class<T> type, TypeRef<?> ... parameters) {
        return of(type, Arrays.asList(parameters));
    }

    /**
     * @param type type for the type reference
     * @param firstParameter first parameter for the type reference
     * @param parameters other parameters for the type reference
     * @return type reference for the specified parameters
     * @param <T> type for the type reference
     */
    public static <T> TypeRef<T> of(Class<T> type, Class<?> firstParameter, Class<?> ... parameters) {
        if(parameters.length == 0) {
            return of(type, TypeRef.of(firstParameter));
        }
        return of(type, Stream.concat(
                        Stream.of(firstParameter),
                        Arrays.stream(parameters))
                .map(TypeRef::of)
                .collect(Collectors.toList()));
    }
}
