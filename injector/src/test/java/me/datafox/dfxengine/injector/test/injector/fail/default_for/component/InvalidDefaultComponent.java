package me.datafox.dfxengine.injector.test.injector.fail.default_for.component;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.test.injector.fail.default_for.NonInheritedClass;

/**
 * @author datafox
 */
@Component(defaultFor = NonInheritedClass.class)
public class InvalidDefaultComponent {
}
