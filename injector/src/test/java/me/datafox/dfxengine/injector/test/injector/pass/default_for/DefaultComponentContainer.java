package me.datafox.dfxengine.injector.test.injector.pass.default_for;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class DefaultComponentContainer {
    @Component(defaultFor = OverrodeNonComponentInterface.class)
    public DefaultNonComponent getDefaultNonComponent() {
        return new DefaultNonComponent();
    }

    @Component
    public OverrodeNonComponent getOverrodeNonComponent() {
        return new OverrodeNonComponent();
    }
}
