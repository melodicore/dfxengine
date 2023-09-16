package me.datafox.dfxengine.injector.test.injector.pass.default_for;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component(defaultFor = OtherOverrodeComponentInterface.class)
public class OtherDefaultComponent implements OtherOverrodeComponentInterface {

}
