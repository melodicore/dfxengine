package me.datafox.dfxengine.injector.test.classes.pass.array;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
@Getter
public class ConstructorArrayComponent {
    private final String[] sarray;
    private final int[] iarray;

    @Inject
    private ConstructorArrayComponent(String[] sarray, int[] iarray) {
        this.sarray = sarray;
        this.iarray = iarray;
    }
}
