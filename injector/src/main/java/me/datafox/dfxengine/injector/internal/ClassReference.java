package me.datafox.dfxengine.injector.internal;

import lombok.*;
import me.datafox.dfxengine.injector.TypeRef;

import java.util.List;


/**
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

    public boolean isAssignableFrom(ClassReference<?> other) {
        if(typeRef.equals(other.typeRef)) {
            return true;
        }
        if(typeRef.isAssignableFrom(other.typeRef)) {
            return true;
        }
        return other.superclasses.stream().anyMatch(getTypeRef()::isAssignableFrom);
    }

    public String getName() {
        return typeRef.toString();
    }

    public static ClassReference<Object> object() {
        return builder()
                .typeRef(TypeRef.object())
                .build();
    }
}
