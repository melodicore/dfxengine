package me.datafox.dfxengine.injector.test;

import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.InjectorImpl;
import me.datafox.dfxengine.injector.api.TypeRef;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.test.classes.fail.constructor.multiple.ComponentWithMultipleConstructors;
import me.datafox.dfxengine.injector.test.classes.fail.constructor.none.ComponentWithNoConstructor;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.cyclic.CyclicComponent1;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.invalid.ComponentWithInvalidDependency;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.multiple.ComponentMethod2;
import me.datafox.dfxengine.injector.test.classes.fail.final_field.ComponentWithFinalFieldDependency;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_class.UnresolvedParameterComponent;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_method.UnresolvedParameterComponentMethod;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_method_class.unresolved_method.UnresolvedParameterComponentMethodClass;
import me.datafox.dfxengine.injector.test.classes.fail.parametric_event.ParametricEventComponent;
import me.datafox.dfxengine.injector.test.classes.pass.array.ConstructorArrayComponent;
import me.datafox.dfxengine.injector.test.classes.pass.array.FieldArrayComponent;
import me.datafox.dfxengine.injector.test.classes.pass.array.InitializeArrayComponent;
import me.datafox.dfxengine.injector.test.classes.pass.array.PrimitiveArrayMethodComponent;
import me.datafox.dfxengine.injector.test.classes.pass.array_parameter.NonComponent3;
import me.datafox.dfxengine.injector.test.classes.pass.array_parameter.Parametric2;
import me.datafox.dfxengine.injector.test.classes.pass.basic.Component;
import me.datafox.dfxengine.injector.test.classes.pass.basic.ComponentMethod;
import me.datafox.dfxengine.injector.test.classes.pass.basic.NonComponent;
import me.datafox.dfxengine.injector.test.classes.pass.basic.StaticNonComponent;
import me.datafox.dfxengine.injector.test.classes.pass.event.EventHandlerComponent;
import me.datafox.dfxengine.injector.test.classes.pass.event.ParametricPredicateEvent;
import me.datafox.dfxengine.injector.test.classes.pass.inheritance_order.ComponentInterface;
import me.datafox.dfxengine.injector.test.classes.pass.initialize.ComponentWithInitialize;
import me.datafox.dfxengine.injector.test.classes.pass.initialize.ComponentWithStaticInitialize;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleComponent;
import me.datafox.dfxengine.injector.test.classes.pass.list.MultipleDependComponent;
import me.datafox.dfxengine.injector.test.classes.pass.method_field.Component4;
import me.datafox.dfxengine.injector.test.classes.pass.method_field.NonComponent4;
import me.datafox.dfxengine.injector.test.classes.pass.order.Component2;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.ExtendingParametricComponent;
import me.datafox.dfxengine.injector.test.classes.pass.parametric.Parametric;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.NonComponent2;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.PerInstanceComponent;
import me.datafox.dfxengine.injector.test.classes.pass.per_instance.RequestingComponent;
import me.datafox.dfxengine.injector.test.classes.pass.primitive.PrimitiveComponentMethod;
import me.datafox.dfxengine.injector.test.classes.pass.primitive.PrimitiveDependencyComponent;
import me.datafox.dfxengine.injector.test.classes.pass.void_component.VoidComponentMethod;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class InjectorTest {
    private static InjectorImpl emptyInjector() {
        return new InjectorBuilder()
                .whitelistPackage("me.datafox.dfxengine.injector.test.classes.empty")
                .build();
    }

    private static InjectorImpl injector(Class<?> classFromPackageToWhitelist) {
        return new InjectorBuilder()
                .whitelistPackageRegex(Pattern.quote(classFromPackageToWhitelist.getPackageName()) + ".*")
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
    }

    @Test
    public void parametricTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(
                TypeRef.of(Parametric.class, Number.class, String.class)));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(
                TypeRef.of(Parametric.class, Number.class, StringBuilder.class)));
        var c3 = assertDoesNotThrow(() -> injector.getComponent(ExtendingParametricComponent.class));
        assertThrows(NoDependenciesPresentException.class, () -> injector.getComponent(
                TypeRef.of(Parametric.class, StringBuilder.class, CharSequence.class)));
        var c4 = assertDoesNotThrow(() -> injector.getComponent(
                TypeRef.of(Parametric.class, StringBuilder.class, Object.class)));
        var c5 = assertDoesNotThrow(() -> injector.getComponent(
                TypeRef.of(Parametric.class, String.class, Appendable.class)));
        var l1 = assertDoesNotThrow(() -> injector.getComponents(
                TypeRef.of(Parametric.class, Number.class, CharSequence.class)));
        var l2 = assertDoesNotThrow(() -> injector.getComponents(
                TypeRef.of(Parametric.class, Object.class, Object.class)));
        var l3 = assertDoesNotThrow(() -> injector.getComponents(
                TypeRef.of(Parametric.class, Number.class, Double.class)));
        var l4 = assertDoesNotThrow(() -> injector.getComponents(
                TypeRef.of(Parametric.class, TypeRef.of(Object.class), TypeRef.of(StringBuilder.class, true))
        ));

        assertEquals("extending", c1.getId());
        assertEquals("method", c2.getId());
        assertEquals("extending", c3.getId());
        assertEquals("vague", c4.getId());
        assertEquals("appendable", c5.getId());

        assertEquals(c1, c3);
        assertEquals(2, l1.size());
        assertEquals(4, l2.size());
        assertTrue(l3.isEmpty());
        assertEquals(3, l4.size());
        assertTrue(l1.contains(c1));
        assertTrue(l1.contains(c2));
        assertNotEquals(l1.indexOf(c1), l1.indexOf(c2));
        assertTrue(l2.contains(c1));
        assertTrue(l2.contains(c2));
        assertTrue(l2.contains(c4));
        assertTrue(l2.contains(c5));
        assertNotEquals(l2.indexOf(c1), l2.indexOf(c2));
        assertNotEquals(l2.indexOf(c1), l2.indexOf(c4));
        assertNotEquals(l2.indexOf(c2), l2.indexOf(c4));
        assertTrue(l4.contains(c2));
        assertTrue(l4.contains(c4));
        assertTrue(l4.contains(c5));

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
    public void orderTest() {
        var injector = assertDoesNotThrow(() -> injector(Component2.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(Component2.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(Component2.class));

        assertEquals(3, l.size());
        assertEquals(c, l.get(0));
    }

    @Test
    public void inheritanceOrderTest() {
        var injector = assertDoesNotThrow(() -> injector(ComponentInterface.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(ComponentInterface.class));
        var l = assertDoesNotThrow(() -> injector.getComponents(ComponentInterface.class));

        assertTrue(l.contains(c));
        assertEquals(2, l.size());
    }

    @Test
    public void initializeTest() {
        var injector = assertDoesNotThrow(() -> injector(ComponentWithInitialize.class));

        var l = assertDoesNotThrow(() -> injector.getComponents(ComponentWithInitialize.class));
        var l1 = l.get(0).getList();
        var l2 = l.get(1).getList();

        assertEquals(List.of("first", "second", "third"), l1);
        assertEquals(List.of(), l2);
        assertEquals("success", ComponentWithStaticInitialize.getStr());
    }

    @Test
    public void arrayParameterTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric2.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric2.class, int[].class)));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric2.class, NonComponent3[].class)));

        assertNotNull(c1);
        assertNotNull(c2);
        assertNotEquals(c1, c2);
    }

    @Test
    public void primitiveTest() {
        var injector = assertDoesNotThrow(() -> injector(PrimitiveComponentMethod.class));

        var c = assertDoesNotThrow(() -> injector.getComponent(PrimitiveDependencyComponent.class));
        var i = assertDoesNotThrow(() -> injector.getComponent(Integer.class));

        assertEquals(5, i);
        assertEquals(5, c.getPrimitive());
        assertEquals(5, c.getBoxed());
    }

    @Test
    public void methodFieldTest() {
        var injector = assertDoesNotThrow(() -> injector(Component4.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(Component4.class));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(NonComponent4.class));

        assertNotNull(c1);
        assertEquals(c1, c2.getComponent());
    }

    @Test
    public void voidComponentTest() {
        var injector = assertDoesNotThrow(() -> injector(VoidComponentMethod.class));
    }

    @Test
    public void constructorTest() {
        assertThrows(ComponentClassWithMultipleValidConstructorsException.class, () -> injector(ComponentWithMultipleConstructors.class));
        assertThrows(ComponentClassWithNoValidConstructorsException.class, () -> injector(ComponentWithNoConstructor.class));
    }

    @Test
    public void arrayTest() {
        var injector = injector(PrimitiveArrayMethodComponent.class);

        String[] sarray = injector.getComponent(String[].class);
        int[] iarray = injector.getComponent(TypeRef.of(int[].class));
        assertSame(sarray, injector.getComponent(ConstructorArrayComponent.class).getSarray());
        assertSame(iarray, injector.getComponent(ConstructorArrayComponent.class).getIarray());
        assertSame(sarray, injector.getComponent(FieldArrayComponent.class).getSarray());
        assertSame(iarray, injector.getComponent(FieldArrayComponent.class).getIarray());
        assertSame(sarray, injector.getComponent(InitializeArrayComponent.class).getSarray());
        assertSame(iarray, injector.getComponent(InitializeArrayComponent.class).getIarray());
    }

    @Test
    public void eventTest() {
        var injector = injector(EventHandlerComponent.class);

        var c = injector.getComponent(EventHandlerComponent.class);

        assertEquals(0, c.events);
        assertEquals(0, c.superEvents);
        assertEquals(0, c.interfaceEvents);

        injector.invokeEvent("yo");
        assertEquals(1, c.events);
        assertEquals(0, c.superEvents);
        assertEquals(0, c.interfaceEvents);

        injector.invokeEvent(52);
        assertEquals(2, c.events);
        assertEquals(0, c.superEvents);
        assertEquals(1, c.interfaceEvents);

        injector.invokeEvent(new ParametricPredicateEvent<>(CharSequence.class));
        assertEquals(4, c.events);
        assertEquals(0, c.superEvents);
        assertEquals(1, c.interfaceEvents);

        injector.invokeEvent(59d);
        assertEquals(4, c.events);
        assertEquals(1, c.superEvents);
        assertEquals(2, c.interfaceEvents);
    }

    @Test
    public void finalFieldTest() {
        assertThrows(FinalFieldDependencyException.class, () -> injector(ComponentWithFinalFieldDependency.class));
    }

    @Test
    public void dependencyTest() {
        assertThrows(NoDependenciesPresentException.class, () -> injector(ComponentWithInvalidDependency.class));
        assertThrows(MultipleDependenciesPresentException.class, () -> injector(ComponentMethod2.class));
        assertThrows(CyclicDependencyException.class, () -> injector(CyclicComponent1.class));
        assertThrows(NoDependenciesPresentException.class, () -> emptyInjector().getComponent(Component.class));
        assertThrows(MultipleDependenciesPresentException.class, () -> injector(MultipleDependComponent.class).getComponent(MultipleComponent.class));
    }

    @Test
    public void parameterTest() {
        assertThrows(UnresolvedOrUnknownTypeException.class, () -> injector(UnresolvedParameterComponent.class));
        assertThrows(UnresolvedOrUnknownTypeException.class, () -> injector(UnresolvedParameterComponentMethod.class));
        assertThrows(UnresolvedOrUnknownTypeException.class, () -> injector(UnresolvedParameterComponentMethodClass.class));
    }

    @Test
    public void getComponentParameterTest() {
        var injector = emptyInjector();

        assertThrows(ParameterCountMismatchException.class, () -> injector.getComponent(TypeRef.of(Function.class, String.class)));
        assertThrows(ParameterCountMismatchException.class, () -> injector.getComponent(TypeRef.of(Supplier.class, Consumer.class)));
        assertThrows(ParameterCountMismatchException.class, () -> injector.getComponent(Supplier.class));
    }

    @Test
    public void parametricEventTest() {
        var injector = injector(ParametricEventComponent.class);

        assertThrows(ParametricEventWithoutInterfaceException.class, () -> injector.invokeEvent((Predicate<String>) s -> false));
    }
}
