package me.datafox.dfxengine.injector.test.classes.pass.initialize;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class ComponentWithStaticInitialize {
    @Getter
    private static String str = "";

    @Initialize
    private static void initialize() {
        str = "success";
    }
}
