package me.datafox.dfxengine.injector.test.classes.pass.list;

import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * @author datafox
 */
public class MethodComponentClass {
    @Component
    private List<NonComponentClass> list() {
        return List.of(new NonComponentClass(), new NonComponentClass());
    }

    @Component
    private MultipleComponentClass single() {
        return new MultipleComponentClass();
    }
}
