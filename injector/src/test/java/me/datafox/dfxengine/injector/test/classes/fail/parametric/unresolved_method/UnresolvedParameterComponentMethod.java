package me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_method;

import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.Optional;

/**
 * @author datafox
 */
public class UnresolvedParameterComponentMethod {
    @Component
    private <T> Optional<T> getOptional() {
        return Optional.empty();
    }
}
