package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import lombok.Getter;

/**
 * @author datafox
 */
@Getter
public class NonComponent2 {
    private final PerInstanceComponent perInstanceComponent;

    public NonComponent2(PerInstanceComponent perInstanceComponent) {
        this.perInstanceComponent = perInstanceComponent;
    }
}
