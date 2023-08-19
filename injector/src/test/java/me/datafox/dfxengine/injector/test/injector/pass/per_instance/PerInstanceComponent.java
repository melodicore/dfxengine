package me.datafox.dfxengine.injector.test.injector.pass.per_instance;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.InstantiationDetails;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component(InstantiationPolicy.PER_INSTANCE)
public class PerInstanceComponent {
    @Getter
    private final Class<?> requestingClass;

    @Inject
    private PerInstanceComponent(InstantiationDetails perInstanceDetails) {
        this.requestingClass = perInstanceDetails.getRequestingType();
    }
}
