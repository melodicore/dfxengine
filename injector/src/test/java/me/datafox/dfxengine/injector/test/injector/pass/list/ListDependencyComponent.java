package me.datafox.dfxengine.injector.test.injector.pass.list;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component
public class ListDependencyComponent {
    @Getter
    private final List<MultipleComponentInterface> list;

    @Inject
    private ListDependencyComponent(List<MultipleComponentInterface> list) {
        this.list = list;
    }
}
