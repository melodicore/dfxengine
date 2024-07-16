package me.datafox.dfxengine.injector.internal;

import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.injector.api.TypeRef;

import java.util.List;

/**
 * @author datafox
 */
@Setter
@Getter
public class SelfReference<T> extends ClassReference<T> {
    private ClassReference<T> reference;

    public SelfReference() {
        super(null, null, null, null, false, null);
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
        if(getParameters().isEmpty() && other.getParameters().isEmpty() && getType().isAssignableFrom(other.getType())) {
            return true;
        }
        if(getParameters().size() == other.getParameters().size() && getType().equals(other.getType())) {
            boolean success = true;
            for(int i = 0; i < getParameters().size(); i++) {
                if(!getParameters().get(i).getType().isAssignableFrom(other.getParameters().get(i).getType())) {
                    success = false;
                }
            }
            if(success) {
                return true;
            }
        }
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
