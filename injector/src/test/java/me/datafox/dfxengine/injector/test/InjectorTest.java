package me.datafox.dfxengine.injector.test;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.Parameter;
import me.datafox.dfxengine.injector.exception.ComponentWithMultipleOptionsForSingletonDependency;
import me.datafox.dfxengine.injector.test.classes.pass.array.NonComponentClass3;
import me.datafox.dfxengine.injector.test.classes.pass.array.ParametricClass2;
import me.datafox.dfxengine.injector.test.classes.pass.basic.ComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.basic.ComponentMethodClass;
import me.datafox.dfxengine.injector.test.classes.pass.basic.NonComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.basic.StaticNonComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.default_impl.ComponentInterface;
import me.datafox.dfxengine.injector.test.classes.pass.initialize.ComponentWithInitializerClass;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleDependComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.order.ComponentClass2;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.ExtendingParametricComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.ParametricClass;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.NonComponentClass2;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.PerInstanceComponentClass;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.RequestingComponentClass;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class InjectorTest {
    private static Injector emptyInjector() {
        return Injector
                .builder()
                .whitelistPackage("me.datafox.dfxengine.injector.test.classes.empty")
                .closeScan(false)
                .build();
    }

    private static Injector injector(Class<?> classFromPackageToWhitelist) {
        return Injector
                .builder()
                .whitelistPackageRegex(Pattern.quote(classFromPackageToWhitelist.getPackageName()) + ".*")
                .closeScan(false)
                .build();
    }

    @Test
    public void basicTest() {
        var injector = injector(ComponentClass.class);

        var cc = assertDoesNotThrow(() -> injector.getComponent(ComponentClass.class));
        var cmc = assertDoesNotThrow(() -> injector.getComponent(ComponentMethodClass.class));
        var ncc = assertDoesNotThrow(() -> injector.getComponent(NonComponentClass.class));
        var sncc = assertDoesNotThrow(() -> injector.getComponent(StaticNonComponentClass.class));

        assertNotNull(cc);
        assertNotNull(cmc);
        assertNotNull(ncc);
        assertNotNull(sncc);
        assertEquals(ncc, cc.getNonComponentClass());
        assertEquals(cmc, cc.getComponentMethodClass());
        assertEquals(sncc, ComponentClass.getStaticNonComponentClass());
    }

    @Test
    public void listTest() {
        var injector = injector(MultipleDependComponentClass.class);

        var c = assertDoesNotThrow(() -> injector.getComponent(MultipleDependComponentClass.class));

        assertEquals(4, c.getComponents().size());
        assertEquals(2, c.getSomeComponents().size());
        assertTrue(c.getComponents().containsAll(c.getSomeComponents()));

        assertThrows(ComponentWithMultipleOptionsForSingletonDependency.class,
                () -> injector.getComponent(MultipleComponentClass.class));
    }

    @Test
    public void parametricTest() {
        var injector = injector(ParametricClass.class);

        var c1 = assertDoesNotThrow(() -> injector.getComponent(ParametricClass.class,
                List.of(Parameter.of(Number.class), Parameter.of(String.class))));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(ParametricClass.class,
                List.of(Parameter.of(Number.class), Parameter.of(StringBuilder.class))));
        var c3 = assertDoesNotThrow(() -> injector.getComponent(ExtendingParametricComponentClass.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(ParametricClass.class,
                List.of(Parameter.of(Number.class), Parameter.of(CharSequence.class))));

        assertEquals(c1, c3);
        assertEquals(2, l.size());
        assertTrue(l.contains(c1));
        assertTrue(l.contains(c2));
        assertNotEquals(l.indexOf(c1), l.indexOf(c2));
    }

    @Test
    public void perInstanceTest() {
        var injector = injector(PerInstanceComponentClass.class);

        var rcc = assertDoesNotThrow(() -> injector.getComponent(RequestingComponentClass.class));
        var ncc = assertDoesNotThrow(() -> injector.getComponent(NonComponentClass2.class));

        assertNotEquals(rcc.getPicc(), ncc.getPerInstanceComponent());
        assertEquals(RequestingComponentClass.class, rcc.getPicc().getRequester());
        assertEquals(NonComponentClass2.class, ncc.getPerInstanceComponent().getRequester());
        assertEquals(PerInstanceComponentClass.class, rcc.getPicc().getChainedPerInstanceComponent().getRequester());
        assertEquals(PerInstanceComponentClass.class, ncc.getPerInstanceComponent().getChainedPerInstanceComponent().getRequester());
    }

    @Test
    public void defaultImplTest() {
        var injector = injector(ComponentInterface.class);

        var c = assertDoesNotThrow(() -> injector.getComponent(ComponentInterface.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(ComponentInterface.class));

        assertTrue(l.contains(c));
        assertEquals(2, l.size());
    }

    @Test
    public void orderTest() {
        var injector = injector(ComponentClass2.class);

        var c = assertDoesNotThrow(() -> injector.getComponent(ComponentClass2.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(ComponentClass2.class));

        assertEquals(3, l.size());
        assertEquals(c, l.get(0));
    }

    @Test
    public void initializeTest() {
        var injector = injector(ComponentWithInitializerClass.class);

        var l = assertDoesNotThrow(() -> injector.getComponent(ComponentWithInitializerClass.class).getList());

        assertEquals(List.of("first", "second", "third"), l);
    }

    @Test
    public void arrayParameterTest() {
        var injector = injector(ParametricClass2.class);

        var c1 = assertDoesNotThrow(() -> injector.getComponent(ParametricClass2.class, Parameter.listOf(int[].class)));

        var c2 = assertDoesNotThrow(() -> injector.getComponent(ParametricClass2.class, Parameter.listOf(Array.class, Parameter.of(NonComponentClass3.class))));
    }
}
