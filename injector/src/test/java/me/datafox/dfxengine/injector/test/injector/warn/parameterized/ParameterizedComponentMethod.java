package me.datafox.dfxengine.injector.test.injector.warn.parameterized;

import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.Optional;

/**
 * @author datafox
 */
public class ParameterizedComponentMethod<T> {
    @SuppressWarnings("rawtypes")
    @Component
    public Optional<String> optionalComponent(ParameterizedComponent component) {
        return Optional.of("test");
    }

    @Component
    public String stringComponent(ParameterizedComponent<T> component) {
        return "test";
    }
}
