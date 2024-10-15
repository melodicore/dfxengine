package me.datafox.dfxengine.injector.test.classes.pass.event;

import me.datafox.dfxengine.injector.api.ParametricEvent;
import me.datafox.dfxengine.injector.api.TypeRef;

import java.util.function.Predicate;

/**
 * @author datafox
 */
public class ParametricPredicateEvent<T> implements Predicate<T>, ParametricEvent {
    private final Class<T> param;

    public ParametricPredicateEvent(Class<T> param) {
        this.param = param;
    }

    @Override
    public boolean test(T t) {
        return false;
    }

    @Override
    public TypeRef<?> getType() {
        return TypeRef.of(ParametricPredicateEvent.class, param);
    }
}
