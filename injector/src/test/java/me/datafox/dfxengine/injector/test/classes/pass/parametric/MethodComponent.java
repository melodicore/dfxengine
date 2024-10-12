package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class MethodComponent {
    @Component
    private Parametric<Number, StringBuilder> getParametricClass() {
        return new Parametric<>("method");
    }

    @Component
    private Parametric<String, ? super CharSequence> getVagueParametricClass() {
        return new Parametric<>("vague");
    }

    @Component
    private Parametric<String, Appendable> getAppendable() {
        return new Parametric<>("appendable");
    }
}
