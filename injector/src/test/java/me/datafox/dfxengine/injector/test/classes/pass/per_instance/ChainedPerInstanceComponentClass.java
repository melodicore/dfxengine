package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import lombok.Getter;
import me.datafox.dfxengine.injector.InstantiationDetails;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component(InstantiationPolicy.PER_INSTANCE)
public class ChainedPerInstanceComponentClass {
    private final Class<?> requester;

    @Inject
    public ChainedPerInstanceComponentClass(InstantiationDetails details) {
        requester = details.getRequestingType();
    }
}
