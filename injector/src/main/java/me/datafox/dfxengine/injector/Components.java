package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains preset {@link Component Components}, currently only {@link Logger}.
 *
 * @author datafox
 */
public class Components {
    @Component(value = InstantiationPolicy.PER_INSTANCE, defaultFor = Logger.class)
    private static Logger createLogger(InstantiationDetails<?,?> instantiationDetails) {
        return LoggerFactory.getLogger(instantiationDetails.getRequestingType());
    }
}
