package me.datafox.dfxengine.injector.test.classes.pass.primitive;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component
public class PrimitiveDependencyComponent {
    @Inject
    private int primitive;

    @Inject
    private Integer boxed;
}
