package me.datafox.dfxengine.injector.test.classes.pass.basic;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@me.datafox.dfxengine.injector.api.annotation.Component
public class Component {
    @Inject
    private NonComponent nonComponentClass;

    private final ComponentMethod componentMethodClass;

    @Getter
    @Inject
    private static StaticNonComponent staticNonComponentClass;

    @Inject
    private Component(ComponentMethod componentMethodClass) {
        this.componentMethodClass = componentMethodClass;

    }
}
