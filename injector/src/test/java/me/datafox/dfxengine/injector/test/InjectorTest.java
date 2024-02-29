package me.datafox.dfxengine.injector.test;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.TypeRef;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.test.classes.fail.array.constructor.ConstructorArrayComponent;
import me.datafox.dfxengine.injector.test.classes.fail.array.field.FieldArrayComponent;
import me.datafox.dfxengine.injector.test.classes.fail.array.initialize.InitializeArrayComponent;
import me.datafox.dfxengine.injector.test.classes.fail.array.object.ObjectArrayMethodComponent;
import me.datafox.dfxengine.injector.test.classes.fail.array.primitive.PrimitiveArrayMethodComponent;
import me.datafox.dfxengine.injector.test.classes.fail.constructor.multiple.ComponentWithMultipleConstructors;
import me.datafox.dfxengine.injector.test.classes.fail.constructor.none.ComponentWithNoConstructor;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.cyclic.CyclicComponent1;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.invalid.ComponentWithInvalidDependency;
import me.datafox.dfxengine.injector.test.classes.fail.dependency.multiple.ComponentMethod2;
import me.datafox.dfxengine.injector.test.classes.fail.final_field.ComponentWithFinalFieldDependency;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_class.UnresolvedParameterComponent;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_method.UnresolvedParameterComponentMethod;
import me.datafox.dfxengine.injector.test.classes.fail.parametric.unresolved_method_class.unresolved_method.UnresolvedParameterComponentMethodClass;
import me.datafox.dfxengine.injector.test.classes.pass.array.NonComponent3;
import me.datafox.dfxengine.injector.test.classes.pass.array.Parametric2;
import me.datafox.dfxengine.injector.test.classes.pass.basic.Component;
import me.datafox.dfxengine.injector.test.classes.pass.basic.ComponentMethod;
import me.datafox.dfxengine.injector.test.classes.pass.basic.NonComponent;
import me.datafox.dfxengine.injector.test.classes.pass.basic.StaticNonComponent;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
    }

    @Test
    public void parametricTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric.class,
                TypeRef.of(Number.class), TypeRef.of(String.class))));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric.class,
                TypeRef.of(Number.class), TypeRef.of(StringBuilder.class))));
        var c3 = assertDoesNotThrow(() -> injector.getComponent(ExtendingParametricComponent.class));
        var l1 = assertDoesNotThrow(() -> injector.getComponents(TypeRef.of(Parametric.class,
                TypeRef.of(Number.class), TypeRef.of(CharSequence.class))));
        var l2 = assertDoesNotThrow(() -> injector.getComponents(TypeRef.of(Parametric.class,
                TypeRef.of(Object.class), TypeRef.of(Object.class))));

        assertEquals(c1, c3);
        assertEquals(2, l1.size());
        assertEquals(3, l2.size());
        assertTrue(l1.contains(c1));
        assertTrue(l1.contains(c2));
        assertNotEquals(l1.indexOf(c1), l1.indexOf(c2));
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

        var l = assertDoesNotThrow(() -> injector.getComponent(ComponentWithInitialize.class).getList());

        assertEquals(List.of("first", "second", "third"), l);
        assertEquals("success", ComponentWithStaticInitialize.getStr());
    }

    @Test
    public void arrayParameterTest() {
        var injector = assertDoesNotThrow(() -> injector(Parametric2.class));

        var c1 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric2.class, TypeRef.of(int[].class))));
        var c2 = assertDoesNotThrow(() -> injector.getComponent(TypeRef.of(Parametric2.class, TypeRef.of(Array.class, TypeRef.of(NonComponent3.class)))));

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
    public void constructorTest() {
        assertThrows(ComponentClassWithMultipleValidConstructorsException.class, () -> injector(ComponentWithMultipleConstructors.class));
        assertThrows(ComponentClassWithNoValidConstructorsException.class, () -> injector(ComponentWithNoConstructor.class));
    }

    @Test
    public void arrayTest() {
        assertThrows(ArrayComponentException.class, () -> injector(PrimitiveArrayMethodComponent.class));
        assertThrows(ArrayComponentException.class, () -> injector(ObjectArrayMethodComponent.class));
        assertThrows(ArrayComponentException.class, () -> injector(ConstructorArrayComponent.class));
        assertThrows(ArrayComponentException.class, () -> injector(FieldArrayComponent.class));
        assertThrows(ArrayComponentException.class, () -> injector(InitializeArrayComponent.class));
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

        assertThrows(ParameterCountMismatchException.class, () -> injector.getComponent(TypeRef.of(Function.class, TypeRef.of(String.class))));
        assertThrows(ParameterCountMismatchException.class, () -> injector.getComponent(TypeRef.of(Supplier.class, TypeRef.of(Consumer.class))));
    }
}
