package me.datafox.dfxengine.injector.test.classes.pass.list;

import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * @author datafox
 */
public class MethodComponent {
    @Component
    private List<NonComponent> list() {
        return List.of(new NonComponent(), new NonComponent());
    }

    @Component
    private MultipleComponent single() {
        return new MultipleComponent();
    }
}
