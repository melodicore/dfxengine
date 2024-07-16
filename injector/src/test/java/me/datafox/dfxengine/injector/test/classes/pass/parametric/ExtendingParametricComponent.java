package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class ExtendingParametricComponent extends Parametric<Double,String> implements ParametricInterface<Double> {
    @Initialize
    private void init(Parametric<? extends Comparable<Double>,String> parametric) {
    }
}
