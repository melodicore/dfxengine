package me.datafox.dfxengine.injector.test.classes.pass.inheritance_order;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class DefaultComponent implements ComponentInterface {
}
