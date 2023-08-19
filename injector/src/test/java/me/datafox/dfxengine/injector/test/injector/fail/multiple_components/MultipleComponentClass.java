package me.datafox.dfxengine.injector.test.injector.fail.multiple_components;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class MultipleComponentClass {
    @Getter
    private final MultipleComponentFailInterface multipleComponentFailInterface;

    @Inject
    private MultipleComponentClass(MultipleComponentFailInterface multipleComponentFailInterface) {
        this.multipleComponentFailInterface = multipleComponentFailInterface;
    }
}
