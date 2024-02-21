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
public class PerInstanceComponentClass {
    private final Class<?> requester;
    private final ChainedPerInstanceComponentClass chainedPerInstanceComponent;

    @Inject
    public PerInstanceComponentClass(InstantiationDetails details, ChainedPerInstanceComponentClass chainedPerInstanceComponent) {
        requester = details.getRequestingType();
        this.chainedPerInstanceComponent = chainedPerInstanceComponent;
    }
}
