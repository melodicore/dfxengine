package me.datafox.dfxengine.injector.test.classes.pass.initialize;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ComponentClass3 {
    @Inject
    private ComponentWithInitializerClass componentClassWithInitializer;
}
