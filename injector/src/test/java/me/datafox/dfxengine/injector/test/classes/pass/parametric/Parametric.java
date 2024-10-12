package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author datafox
 */
@AllArgsConstructor
@Getter
public class Parametric<T, E> implements ParametricInterface<T> {
    private final String id;

    @Override
    public String toString() {
        return id;
    }
}
