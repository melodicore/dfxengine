package me.datafox.dfxengine.injector.test.classes.pass.array;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
@Getter
public class InitializeArrayComponent {
    private String[] sarray;
    private int[] iarray;

    @Initialize
    private void initialize(String[] sarray, int[] iarray) {
        this.sarray = sarray;
        this.iarray = iarray;
    }
}
