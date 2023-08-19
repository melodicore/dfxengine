package me.datafox.dfxengine.injector.test.injector.pass.instantiation_details;

import lombok.Getter;
import me.datafox.dfxengine.injector.InstantiationDetails;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component(InstantiationPolicy.PER_INSTANCE)
public class PerInstanceDetailsComponent implements DetailsComponentInterface {
    @Getter
    private final InstantiationDetails instantiationDetails;

    @Inject
    public PerInstanceDetailsComponent(InstantiationDetails instantiationDetails) {
        this.instantiationDetails = instantiationDetails;
    }
}
