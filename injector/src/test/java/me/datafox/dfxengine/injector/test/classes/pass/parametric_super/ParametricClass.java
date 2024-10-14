package me.datafox.dfxengine.injector.test.classes.pass.parametric_super;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public class ParametricClass<T, U> {
    private final String id;
}