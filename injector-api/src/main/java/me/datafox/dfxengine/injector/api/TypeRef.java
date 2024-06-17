package me.datafox.dfxengine.injector.api;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a type with parameters. This class is used when requesting {@link Component Components} from the
 * {@link Injector}. As an example, if you wanted to reference {@code Component<Type1,Type2<Type3>>}, you would call
 * {@code TypeRef.of(Component.class, TypeRef.of(Type1.class), TypeRef.of(Type2.class, TypeRef.of(Type3.class)));}. The
 * constructor checks for parameter count and throws an exception if an invalid amount of type parameters. The one
 * exception is {@link Array}, which is allowed to have one type parameter to represent a generic object array.
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
     * amount of provided parameters. If the type is {@link Array} and there is exactly one type parameter, this
     * exception will not be thrown (see {@link TypeRef})
     */
    public TypeRef(Class<T> type, List<TypeRef<?>> parameters) {
        if((!Array.class.equals(type) || parameters.size() != 1) &&
                type.getTypeParameters().length != parameters.size()) {
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
     * This method is equivalent to {@link Class#isAssignableFrom(Class)} but takes type parameters into account. Only
     * if this type reference and all parameters recursively have an equal amount of parameters and the type and all
     * parameter types return {@code true} for {@link Class#isAssignableFrom(Class)} will this method return
     * {@code true}.
     *
     * @param other type reference to compare to
     * @return {@code true} if this type reference is assignable from the other type reference
     */
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

    /**
     * @return String representation of this type reference in the format "{@code Type<Parameter, Other<Child>>}"
     */
    @Override
    public String toString() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(TypeRef::toString)
                        .collect(Collectors.joining(", ")) + ">"));
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
}
