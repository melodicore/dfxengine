package me.datafox.dfxengine.injector.internal;

import lombok.*;
import me.datafox.dfxengine.injector.api.TypeRef;

import java.util.List;
import java.util.stream.Collectors;


/**
 * A reference to a {@link Class} used internally by this module.
 *
 * @author datafox
 */
@Data
@Builder
public class ClassReference<T> {
    private final Class<T> type;

    @Singular
    private final List<ClassReference<?>> parameters;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final ClassReference<? super T> superclass;

    @EqualsAndHashCode.Exclude
    @Singular
    @ToString.Exclude
    private final List<ClassReference<? super T>> interfaces;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private boolean list = false;

    @EqualsAndHashCode.Exclude
    private ClassReference<?> listReference;

    public ClassReference<?> getActualReference() {
        if(list) {
            return getListReference();
        } else {
            return this;
        }
    }

    /**
     * This method is equivalent to {@link Class#isAssignableFrom(Class)} but takes type parameters into account.
     *
     * @param other class reference to compare to
     * @return {@code true} if this class reference is assignable from the other class reference
     */
    public boolean isAssignableFrom(ClassReference<?> other) {
        if(this.equals(other)) {
            return true;
        }
        if(getParameters().isEmpty() && other.getParameters().isEmpty() && getType().isAssignableFrom(other.getType())) {
            return true;
        }
        if(getParameters().size() == other.getParameters().size() && getType().equals(other.getType())) {
            for(int i = 0; i < getParameters().size(); i++) {
                if(!getParameters().get(i).isAssignableFrom(other.getParameters().get(i))) {
                    return false;
                }
            }
            return true;
        }
        if(other.getSuperclass() == null) {
            if(getType().isAssignableFrom(Object.class)) {
                return true;
            }
        } else if(isAssignableFrom(other.getSuperclass())) {
            return true;
        }
        return other.getInterfaces().stream().anyMatch(this::isAssignableFrom);
    }

    /**
     * @return Signature of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     * without packages
     */
    public String getSignatureWithoutPackage() {
        return type.getSimpleName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(ClassReference::getSignatureWithoutPackage)
                        .collect(Collectors.joining(",")) + ">"));
    }

    /**
     * @return Signature of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     * with the parameters without packages
     */
    public String getSignatureParametersWithoutPackage() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(ClassReference::getSignatureWithoutPackage)
                        .collect(Collectors.joining(",")) + ">"));
    }

    /**
     * @return Signature of this type reference in the format "{@code Type<Parameter,Other<Child>>}"
     */
    public String getSignature() {
        return type.getName() + (parameters.isEmpty() ? "" :
                "<" + (parameters
                        .stream()
                        .map(ClassReference::getSignature)
                        .collect(Collectors.joining(",")) + ">"));
    }

    public TypeRef<T> toTypeRef() {
        return TypeRef.<T>builder()
                .type(type)
                .parameters(parameters
                        .stream()
                        .map(ClassReference::toTypeRef)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * @return class reference for {@link Object}
     */
    public static ClassReference<Object> object() {
        return builder()
                .type(Object.class)
                .build();
    }
}
