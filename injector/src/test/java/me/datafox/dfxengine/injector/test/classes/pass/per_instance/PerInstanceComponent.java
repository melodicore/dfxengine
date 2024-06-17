package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.InstantiationDetails;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component(InstantiationPolicy.PER_INSTANCE)
public class PerInstanceComponent {
    private final Class<?> requester;
    private final ChainedPerInstanceComponent chainedPerInstanceComponent;

    @Inject
    public PerInstanceComponent(InstantiationDetails details, ChainedPerInstanceComponent chainedPerInstanceComponent) {
        requester = details.getRequesting().getType();
        this.chainedPerInstanceComponent = chainedPerInstanceComponent;
    }
}
