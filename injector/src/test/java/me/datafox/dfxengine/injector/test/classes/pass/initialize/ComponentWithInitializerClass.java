package me.datafox.dfxengine.injector.test.classes.pass.initialize;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Getter
@Component
public class ComponentWithInitializerClass {
    private final List<String> list;

    public ComponentWithInitializerClass() {
        list = new ArrayList<>();
    }

    @Initialize
    private void initialize1() {
        list.add("second");
    }

    @Initialize(-5)
    private void initialize2() {
        list.add("first");
    }

    @Initialize(5)
    private void initialize3(ComponentClass3 componentClass3) {
        list.add("third");
    }
}
