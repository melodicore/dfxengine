package me.datafox.dfxengine.injector.test.injector.fail.no_constructor;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component
public class NoValidConstructorComponent {
    @Getter
    private final Object object;

    private NoValidConstructorComponent(Object object) {
        this.object = object;
    }
}
