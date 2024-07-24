package me.datafox.dfxengine.injector.test.classes.pass.initialize;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component
public class Component3 {
    @Inject
    private List<ComponentWithInitialize> componentClassWithInitializer;
}
