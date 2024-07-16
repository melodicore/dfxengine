package me.datafox.dfxengine.injector.test.classes.pass.array;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
@Getter
public class FieldArrayComponent {
    @Inject
    private String[] sarray;
    @Inject
    private int[] iarray;
}
