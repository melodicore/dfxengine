package me.datafox.dfxengine.injector.test.injector.pass.default_for;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component(defaultFor = OverrodeComponentInterface.class)
public class DefaultComponent implements OverrodeComponentInterface {

}
