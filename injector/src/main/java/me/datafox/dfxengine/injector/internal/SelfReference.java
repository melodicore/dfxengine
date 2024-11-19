package me.datafox.dfxengine.injector.internal;

import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.injector.api.TypeRef;

import java.util.List;

/**
 * A reference to a {@link ClassReference} used to avoid cyclic references.
 *
 * @param <T> type of the referenced {@link ClassReference}
 *
 * @author datafox
 */
@Setter
@Getter
@SuppressWarnings("MissingJavadoc")
public class SelfReference<T> extends ClassReference<T> {
    private ClassReference<T> reference;

    public SelfReference() {
        super(null, false, null, null, null, false, null);
    }

    @Override
    public boolean isSup() {
        return reference.isSup();
    }

    @Override
    public Class<T> getType() {
        return reference.getType();
    }

    @Override
    public List<ClassReference<?>> getParameters() {
        return reference.getParameters();
    }

    @Override
    public ClassReference<? super T> getSuperclass() {
        return reference.getSuperclass();
    }

    @Override
    public List<ClassReference<? super T>> getInterfaces() {
        return reference.getInterfaces();
    }

    @Override
    public boolean isList() {
        return reference.isList();
    }

    @Override
    public ClassReference<?> getListReference() {
        return reference.getListReference();
    }

    @Override
    public void setList(boolean list) {
        reference.setList(list);
    }

    @Override
    public void setListReference(ClassReference<?> listReference) {
        reference.setListReference(listReference);
    }

    @Override
    public boolean equals(Object o) {
        return reference.equals(o);
    }

    @Override
    protected boolean canEqual(Object other) {
        return reference.canEqual(other);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public String toString() {
        return reference.toString();
    }

    @Override
    public ClassReference<?> getActualReference() {
        return reference.getActualReference();
    }

    @Override
    public boolean isAssignableFrom(ClassReference<?> other) {
        return reference.isAssignableFrom(other);
    }

    @Override
    public String getSignatureWithoutPackage() {
        return reference.getSignatureWithoutPackage();
    }

    @Override
    public String getSignatureParametersWithoutPackage() {
        return reference.getSignatureParametersWithoutPackage();
    }

    @Override
    public String getSignature() {
        return reference.getSignature();
    }

    @Override
    public TypeRef<T> toTypeRef() {
        return reference.toTypeRef();
    }
}
