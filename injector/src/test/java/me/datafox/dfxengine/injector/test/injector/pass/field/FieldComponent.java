package me.datafox.dfxengine.injector.test.injector.pass.field;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class FieldComponent {
    @Getter
    @Inject
    private FieldComponentDependency fieldComponentDependency;
}
