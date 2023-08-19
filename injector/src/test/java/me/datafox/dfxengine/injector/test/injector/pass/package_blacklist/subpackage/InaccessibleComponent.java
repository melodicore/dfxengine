package me.datafox.dfxengine.injector.test.injector.pass.package_blacklist.subpackage;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.test.injector.pass.package_blacklist.BlacklistComponentInterface;

/**
 * @author datafox
 */
@Component
public class InaccessibleComponent implements BlacklistComponentInterface {
}
