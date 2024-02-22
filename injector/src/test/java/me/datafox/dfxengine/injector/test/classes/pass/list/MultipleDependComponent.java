package me.datafox.dfxengine.injector.test.classes.pass.list;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component
@Getter
public class MultipleDependComponent {
    @Inject
    private List<NonComponent> components;

    @Inject
    private List<MultipleComponent> someComponents;
}
