package me.datafox.dfxengine.injector.test.injector.pass.instantiation_details;

import lombok.Getter;
import me.datafox.dfxengine.injector.InstantiationDetails;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class DetailsComponent implements DetailsComponentInterface {
    @Getter
    private final InstantiationDetails instantiationDetails;

    @Getter
    private final PerInstanceDetailsComponent perInstanceDetailsComponent;

    @Inject
    public DetailsComponent(InstantiationDetails instantiationDetails, PerInstanceDetailsComponent perInstanceDetailsComponent) {
        this.instantiationDetails = instantiationDetails;
        this.perInstanceDetailsComponent = perInstanceDetailsComponent;
    }
}
