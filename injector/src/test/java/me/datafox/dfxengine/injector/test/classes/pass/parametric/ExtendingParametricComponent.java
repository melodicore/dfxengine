package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component
public class ExtendingParametricComponent extends Parametric<Double,String> implements ParametricInterface<Double> {
}
