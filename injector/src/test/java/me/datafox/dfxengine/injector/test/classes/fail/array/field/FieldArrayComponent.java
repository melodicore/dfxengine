package me.datafox.dfxengine.injector.test.classes.fail.array.field;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class FieldArrayComponent {
    @Inject
    private String[] array;
}
