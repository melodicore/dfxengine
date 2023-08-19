package me.datafox.dfxengine.injector.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.test.injector.fail.cyclic_dependency.CyclicDependencyComponent1;
import me.datafox.dfxengine.injector.test.injector.fail.multiple_components.MultipleComponentClass;
import me.datafox.dfxengine.injector.test.injector.fail.multiple_constructors.MultipleConstructorsComponent;
import me.datafox.dfxengine.injector.test.injector.fail.no_constructor.NoValidConstructorComponent;
import me.datafox.dfxengine.injector.test.injector.fail.unknown_component.UnknownDependencyComponent;
import me.datafox.dfxengine.injector.test.injector.pass.class_blacklist.BlacklistComponentClassInterface;
import me.datafox.dfxengine.injector.test.injector.pass.class_blacklist.BlacklistedComponentClass;
import me.datafox.dfxengine.injector.test.injector.pass.class_whitelist.AccessibleComponentClass;
import me.datafox.dfxengine.injector.test.injector.pass.class_whitelist.WhitelistComponentInterface;
import me.datafox.dfxengine.injector.test.injector.pass.combined.CombinedComponent;
import me.datafox.dfxengine.injector.test.injector.pass.constructor.ConstructorComponent;
import me.datafox.dfxengine.injector.test.injector.pass.cyclic_initialization.InitializableDependencyComponent;
import me.datafox.dfxengine.injector.test.injector.pass.field.FieldComponent;
import me.datafox.dfxengine.injector.test.injector.pass.inheritance.ComponentInterface;
import me.datafox.dfxengine.injector.test.injector.pass.inheritance.ComponentSuperclass;
import me.datafox.dfxengine.injector.test.injector.pass.inheritance.InheritingComponent;
import me.datafox.dfxengine.injector.test.injector.pass.initialization.InitializableComponent;
import me.datafox.dfxengine.injector.test.injector.pass.instantiation_details.DetailsComponent;
import me.datafox.dfxengine.injector.test.injector.pass.instantiation_details.DetailsComponentInterface;
import me.datafox.dfxengine.injector.test.injector.pass.instantiation_details.PerInstanceDetailsComponent;
import me.datafox.dfxengine.injector.test.injector.pass.list.ListDependencyComponent;
import me.datafox.dfxengine.injector.test.injector.pass.list.MultipleComponentInterface;
import me.datafox.dfxengine.injector.test.injector.pass.method.NonComponentClass;
import me.datafox.dfxengine.injector.test.injector.pass.package_blacklist.BlacklistComponentInterface;
import me.datafox.dfxengine.injector.test.injector.pass.package_blacklist.subpackage.InaccessibleComponent;
import me.datafox.dfxengine.injector.test.injector.pass.per_instance.RequestingComponent;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class InjectorTest {
    private Injector buildInjector(Class<?> classFromPackage) {
        return InjectorBuilder
                .create()
                .whitelistPackage(classFromPackage.getPackageName())
                .build();
    }

    private Injector emptyInjector() {
        return InjectorBuilder
                .create()
                .whitelistPackage("me.datafox.dfxengine.injector.test.injector.empty")
                .build();
    }

    @Test
    public void packageBlacklist_pass() {
        var injector = InjectorBuilder
                .create()
                .whitelistPackage(BlacklistComponentInterface.class.getPackageName())
                .blacklistPackage(InaccessibleComponent.class.getPackageName())
                .build();

        assertTrue(injector.isSingletonComponent(BlacklistComponentInterface.class));
    }

    @Test
    public void classWhitelist_pass() {
        var injector = InjectorBuilder
                .create()
                .whitelistClass(AccessibleComponentClass.class.getName())
                .build();

        assertTrue(injector.isSingletonComponent(WhitelistComponentInterface.class));
    }

    @Test
    public void classBlacklist_pass() {
        var injector = InjectorBuilder
                .create()
                .whitelistPackage(BlacklistComponentClassInterface.class.getPackageName())
                .blacklistClass(BlacklistedComponentClass.class.getName())
                .build();

        assertTrue(injector.isSingletonComponent(BlacklistComponentClassInterface.class));
    }

    @Test
    public void constructor_pass() {
        var injector = buildInjector(ConstructorComponent.class);

        assertNotNull(injector
                .getSingletonComponent(ConstructorComponent.class)
                .getConstructorComponentDependency());
    }

    @Test
    public void field_pass() {
        var injector = buildInjector(FieldComponent.class);

        assertNotNull(injector
                .getSingletonComponent(FieldComponent.class)
                .getFieldComponentDependency());
    }

    @Test
    public void combined_pass() {
        var injector = buildInjector(CombinedComponent.class);

        var combinedComponent = injector.getSingletonComponent(CombinedComponent.class);

        assertNotNull(combinedComponent.getCombinedComponentConstructorDependency());

        assertNotNull(combinedComponent.getCombinedComponentFieldDependency());
    }

    @Test
    public void method_pass() {
        var injector = buildInjector(NonComponentClass.class);

        var list = injector.getComponents(NonComponentClass.class);

        assertEquals(2, list.size());

        assertInstanceOf(NonComponentClass.class, list.get(0));

        assertInstanceOf(NonComponentClass.class, list.get(1));

        assertNotEquals(list.get(0), list.get(1));
    }

    @Test
    public void perInstance_pass() {
        var injector = buildInjector(RequestingComponent.class);

        var requestingComponent = injector.getSingletonComponent(RequestingComponent.class);

        assertNotNull(requestingComponent.getPerInstanceComponent());

        assertEquals(RequestingComponent.class,
                requestingComponent.getPerInstanceComponent().getRequestingClass());
    }

    @Test
    public void inheritance_pass() {
        var injector = buildInjector(ComponentInterface.class);

        var componentInterface = injector.getSingletonComponent(ComponentInterface.class);
        var componentSuperclass = injector.getSingletonComponent(ComponentSuperclass.class);

        assertInstanceOf(InheritingComponent.class, componentInterface);

        assertInstanceOf(InheritingComponent.class, componentSuperclass);
    }

    @Test
    public void list_pass() {
        var injector = buildInjector(ListDependencyComponent.class);

        var listDependencyComponent = injector.getSingletonComponent(ListDependencyComponent.class);
        var list = injector.getComponents(MultipleComponentInterface.class);

        assertEquals(3, listDependencyComponent.getList().size());

        assertEquals(list, listDependencyComponent.getList());
    }

    @Test
    public void initialization_pass() {
        var injector = buildInjector(InitializableComponent.class);

        var initializableComponent = injector.getSingletonComponent(InitializableComponent.class);

        assertEquals("test", initializableComponent.getString());
    }

    @Test
    public void cyclicInitialization_pass() {
        var injector = buildInjector(InitializableDependencyComponent.class);

        var initializableDependencyComponent = injector.getSingletonComponent(InitializableDependencyComponent.class);

        assertNotNull(initializableDependencyComponent.getOtherDependencyComponent());
    }

    @Test
    public void instantiationDetails_pass() {
        var injector = buildInjector(DetailsComponent.class);

        var detailsComponent = injector.getSingletonComponent(DetailsComponent.class);
        var perInstanceDetailsComponent = detailsComponent.getPerInstanceDetailsComponent();
        var list = injector.getComponents(DetailsComponentInterface.class, Object.class);

        assertEquals(DetailsComponent.class,
                detailsComponent.getInstantiationDetails().getType());

        assertNull(detailsComponent.getInstantiationDetails().getRequestingType());

        assertEquals(PerInstanceDetailsComponent.class,
                perInstanceDetailsComponent.getInstantiationDetails().getType());

        assertEquals(DetailsComponent.class,
                perInstanceDetailsComponent.getInstantiationDetails().getRequestingType());

        assertTrue(list.contains(detailsComponent));

        assertFalse(list.contains(perInstanceDetailsComponent));

        var other = list.get(1 - list.indexOf(detailsComponent));

        assertInstanceOf(PerInstanceDetailsComponent.class, other);

        var castedOther = (PerInstanceDetailsComponent) other;

        assertEquals(DetailsComponentInterface.class,
                castedOther.getInstantiationDetails().getType());

        assertEquals(Object.class,
                castedOther.getInstantiationDetails().getRequestingType());
    }

    @Test
    public void cyclicDependency_fail() {
        assertThrows(CyclicDependencyException.class,
                () -> buildInjector(CyclicDependencyComponent1.class));
    }

    @Test
    public void noConstructor_fail() {
        assertThrows(NoValidConstructorException.class,
                () -> buildInjector(NoValidConstructorComponent.class));

        var injector = emptyInjector();

        assertThrows(NoValidConstructorException.class,
                () -> injector.newInstance(NoValidConstructorComponent.class));
    }

    @Test
    public void multipleConstructors_fail() {
        assertThrows(MultipleInjectConstructorsException.class,
                () -> buildInjector(MultipleConstructorsComponent.class));

        var injector = emptyInjector();

        assertThrows(MultipleInjectConstructorsException.class,
                () -> injector.newInstance(MultipleConstructorsComponent.class));
    }

    @Test
    public void unknownComponent_fail() {
        assertThrows(UnknownComponentException.class,
                () -> buildInjector(UnknownDependencyComponent.class));

        var injector = emptyInjector();

        assertThrows(UnknownComponentException.class,
                () -> injector.newInstance(UnknownDependencyComponent.class));
    }

    @Test
    public void multipleComponents_fail() {
        assertThrows(MultipleValidComponentsException.class,
                () -> buildInjector(MultipleComponentClass.class));

        var injector = InjectorBuilder
                .create()
                .whitelistPackage(MultipleComponentClass.class.getPackageName())
                .blacklistClass(MultipleComponentClass.class.getName())
                .build();

        assertThrows(MultipleValidComponentsException.class,
                () -> injector.newInstance(MultipleComponentClass.class));
    }

    @Test
    public void parameterized_warn() {
        var builder = (InjectorBuilder) InjectorBuilder.create();
        var appender = new TestAppender();

        appender.setContext((Context) LoggerFactory.getILoggerFactory());
        ((Logger) builder.getLogger()).addAppender(appender);
        appender.start();

        builder.whitelistPackage("me.datafox.dfxengine.injector.test.injector.warn.parameterized").build();

        var list = appender.getForLevel(Level.WARN);

        assertEquals(1, list.size());

        ILoggingEvent event = list.get(0);

        assertEquals("ParameterizedInterface.class has a parameterized type, this is not supported and " +
                "may cause runtime exceptions or other unexpected behavior", event.getMessage());
    }
}
