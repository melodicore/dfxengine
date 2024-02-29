package me.datafox.dfxengine.injector.test.classes.pass.method_field;

import lombok.Getter;

/**
 * @author datafox
 */
@Getter
public class NonComponent4 {
    private final Component4 component;

    public NonComponent4(Component4 component) {
        this.component = component;
    }
}
