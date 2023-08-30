package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains preset components, currently only {@link Logger}.
 *
 * @author datafox
 */
public class Components {
    @Component(InstantiationPolicy.PER_INSTANCE)
    private static Logger createLogger(InstantiationDetails<?,?> instantiationDetails) {
        return LoggerFactory.getLogger(instantiationDetails.getRequestingType());
    }
}
