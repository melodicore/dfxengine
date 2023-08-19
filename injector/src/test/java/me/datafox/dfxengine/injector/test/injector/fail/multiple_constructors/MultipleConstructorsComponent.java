package me.datafox.dfxengine.injector.test.injector.fail.multiple_constructors;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class MultipleConstructorsComponent {
    @Getter
    private final DummyComponent1 dummyComponent1;

    @Getter
    private final DummyComponent2 dummyComponent2;

    @Inject
    private MultipleConstructorsComponent(DummyComponent1 dummyComponent1) {
        this.dummyComponent1 = dummyComponent1;
        dummyComponent2 = null;
    }

    @Inject
    private MultipleConstructorsComponent(DummyComponent2 dummyComponent2) {
        dummyComponent1 = null;
        this.dummyComponent2 = dummyComponent2;
    }
}
