package me.datafox.dfxengine.injector.internal;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.injector.Parameter;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author datafox
 */
@Data
@SuperBuilder
public class ClassReference<T> {
    private final Class<T> type;

    @Singular
    private final List<Parameter<?>> parameters;

    @EqualsAndHashCode.Exclude
    @Singular
    @ToString.Exclude
    private final List<ClassReference<? super T>> superclasses;

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
        if(Object.class.equals(type)) {
            return true;
        }
        if(!type.isAssignableFrom(other.type)) {
            return false;
        } if(!type.equals(other.type)) {
            return other.superclasses.stream().anyMatch(this::isAssignableFrom);
        }
        if(parameters.size() != other.parameters.size()) {
            return false;
        }
        for(int i = 0; i < parameters.size(); i++) {
            if(!parameters.get(i).isAssignableFrom(other.parameters.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String getParameterString() {
        return parameters
                .stream()
                .map(Parameter::toString)
                .collect(Collectors.joining(", "));
    }

    public static ClassReference<Object> object() {
        return builder()
                .type(Object.class)
                .build();
    }
}
