package me.datafox.dfxengine.injector.test.classes.pass.basic;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component
public class ComponentClass {
    @Inject
    private NonComponentClass nonComponentClass;

    private final ComponentMethodClass componentMethodClass;

    @Getter
    @Inject
    private static StaticNonComponentClass staticNonComponentClass;

    @Inject
    private ComponentClass(ComponentMethodClass componentMethodClass) {
        this.componentMethodClass = componentMethodClass;

    }
}
