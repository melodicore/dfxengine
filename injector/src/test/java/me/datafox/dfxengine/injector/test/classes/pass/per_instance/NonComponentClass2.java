package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import lombok.Getter;

/**
 * @author datafox
 */
@Getter
public class NonComponentClass2 {
    private final PerInstanceComponentClass perInstanceComponent;

    public NonComponentClass2(PerInstanceComponentClass perInstanceComponent) {
        this.perInstanceComponent = perInstanceComponent;
    }
}
