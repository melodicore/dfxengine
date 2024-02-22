package me.datafox.dfxengine.injector.test;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.Parameter;
import me.datafox.dfxengine.injector.exception.ComponentClassWithMultipleValidConstructorsException;
import me.datafox.dfxengine.injector.exception.ComponentClassWithNoValidConstructorsException;
import me.datafox.dfxengine.injector.exception.MultipleComponentsForSingletonDependencyException;
import me.datafox.dfxengine.injector.test.classes.fail.multiple_constructors.ComponentWithMultipleConstructors;
import me.datafox.dfxengine.injector.test.classes.fail.no_constructor.ComponentWithNoConstructor;
import me.datafox.dfxengine.injector.test.classes.pass.array.NonComponent3;
import me.datafox.dfxengine.injector.test.classes.pass.array.Parametric2;
import me.datafox.dfxengine.injector.test.classes.pass.basic.Component;
import me.datafox.dfxengine.injector.test.classes.pass.basic.ComponentMethod;
import me.datafox.dfxengine.injector.test.classes.pass.basic.NonComponent;
import me.datafox.dfxengine.injector.test.classes.pass.basic.StaticNonComponent;
import me.datafox.dfxengine.injector.test.classes.pass.default_impl.ComponentInterface;
import me.datafox.dfxengine.injector.test.classes.pass.initialize.ComponentWithInitializer;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleComponent;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleDependComponent;
import me.datafox.dfxengine.injector.test.classes.pass.order.Component2;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.ExtendingParametricComponent;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.Parametric;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.NonComponent2;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.PerInstanceComponent;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.RequestingComponent;
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
        var injector = assertDoesNotThrow(() -> injector(Component.class));

        var cc = assertDoesNotThrow(() -> injector.getComponent(Component.class));
        var cmc = assertDoesNotThrow(() -> injector.getComponent(ComponentMethod.class));
        var ncc = assertDoesNotThrow(() -> injector.getComponent(NonComponent.class));
        var sncc = assertDoesNotThrow(() -> injector.getComponent(StaticNonComponent.class));

        assertNotNull(cc);
        assertNotNull(cmc);
        assertNotNull(ncc);
        assertNotNull(sncc);
        assertEquals(ncc, cc.getNonComponentClass());
        assertEquals(cmc, cc.getComponentMethodClass());
        assertEquals(sncc, Component.getStaticNonComponentClass());
    }

    @Test
    public void listTest() {
        var injector = assertDoesNotThrow(() -> injector(MultipleDependComponent.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(MultipleDependComponent.class));

        assertEquals(4, c.getComponents().size());
        assertEquals(2, c.getSomeComponents().size());
        assertTrue(c.getComponents().containsAll(c.getSomeComponents()));

        assertThrows(MultipleComponentsForSingletonDependencyException.class,
                () -> injector.getComponent(MultipleComponent.class));
    }

    @Test
    public void parametricTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(Parametric.class,
                List.of(Parameter.of(Number.class), Parameter.of(String.class))));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(Parametric.class,
                List.of(Parameter.of(Number.class), Parameter.of(StringBuilder.class))));
        var c3 = assertDoesNotThrow(() -> injector.getComponent(ExtendingParametricComponent.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(Parametric.class,
                List.of(Parameter.of(Number.class), Parameter.of(CharSequence.class))));

        assertEquals(c1, c3);
        assertEquals(2, l.size());
        assertTrue(l.contains(c1));
        assertTrue(l.contains(c2));
        assertNotEquals(l.indexOf(c1), l.indexOf(c2));
    }

    @Test
    public void perInstanceTest() {
        var injector = assertDoesNotThrow(() -> injector(PerInstanceComponent.class));

        var rcc = assertDoesNotThrow(() -> injector.getComponent(RequestingComponent.class));
        var ncc = assertDoesNotThrow(() -> injector.getComponent(NonComponent2.class));

        assertNotEquals(rcc.getPicc(), ncc.getPerInstanceComponent());
        assertEquals(RequestingComponent.class, rcc.getPicc().getRequester());
        assertEquals(NonComponent2.class, ncc.getPerInstanceComponent().getRequester());
        assertEquals(PerInstanceComponent.class, rcc.getPicc().getChainedPerInstanceComponent().getRequester());
        assertEquals(PerInstanceComponent.class, ncc.getPerInstanceComponent().getChainedPerInstanceComponent().getRequester());
    }

    @Test
    public void defaultImplTest() {
        var injector = assertDoesNotThrow(() -> injector(ComponentInterface.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(ComponentInterface.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(ComponentInterface.class));

        assertTrue(l.contains(c));
        assertEquals(2, l.size());
    }

    @Test
    public void orderTest() {
        var injector = assertDoesNotThrow(() -> injector(Component2.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(Component2.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(Component2.class));

        assertEquals(3, l.size());
        assertEquals(c, l.get(0));
    }

    @Test
    public void initializeTest() {
        var injector = assertDoesNotThrow(() -> injector(ComponentWithInitializer.class));

        var l = assertDoesNotThrow(() -> injector.getComponent(ComponentWithInitializer.class).getList());

        assertEquals(List.of("first", "second", "third"), l);
    }

    @Test
    public void arrayParameterTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric2.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(Parametric2.class, Parameter.listOf(int[].class)));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(Parametric2.class, Parameter.listOf(Array.class, Parameter.of(NonComponent3.class))));

        assertNotNull(c1);
        assertNotNull(c2);
        assertNotEquals(c1, c2);
    }

    @Test
    public void multipleConstructorsTest() {
        assertThrows(ComponentClassWithMultipleValidConstructorsException.class, () -> injector(ComponentWithMultipleConstructors.class));
    }

    @Test
    public void noConstructorTest() {
        assertThrows(ComponentClassWithNoValidConstructorsException.class, () -> injector(ComponentWithNoConstructor.class));
    }
}
