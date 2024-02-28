package me.datafox.dfxengine.injector.internal;

import lombok.*;
import me.datafox.dfxengine.injector.TypeRef;

import java.util.List;


/**
 * A reference to a {@link Class} used internally by this module.
 *
 * @author datafox
 */
@Data
@Builder
public class ClassReference<T> {
    private final TypeRef<T> typeRef;

    @EqualsAndHashCode.Exclude
    @Singular
    @ToString.Exclude
    private final List<TypeRef<? super T>> superclasses;

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
     * @see TypeRef#isAssignableFrom(TypeRef) 
     */
    public boolean isAssignableFrom(ClassReference<?> other) {
        if(typeRef.equals(other.typeRef)) {
            return true;
        }
        if(typeRef.isAssignableFrom(other.typeRef)) {
            return true;
        }
        return other.superclasses.stream().anyMatch(getTypeRef()::isAssignableFrom);
    }

    /**
     * @return signature of this class reference
     */
    public String getSignature() {
        return typeRef.toString();
    }

    /**
     * @return class reference for {@link Object}
     */
    public static ClassReference<Object> object() {
        return builder()
                .typeRef(TypeRef.object())
                .build();
    }
}
