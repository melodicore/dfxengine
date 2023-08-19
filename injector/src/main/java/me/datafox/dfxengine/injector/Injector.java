package me.datafox.dfxengine.injector;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.collection.FunctionClassMap;
import me.datafox.dfxengine.injector.collection.ObjectClassMap;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.utils.ClassUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * A dependency injector. Instantiates classes and invokes methods. Use {@link InjectorBuilder} to obtain instances of
 * this class.
 * </p>
 * <p>
 * The builder scans all classes and methods annotated with {@link Component}, instantiates/invokes them and registers
 * them as components. Components may have fields and constructors annotated with {@link Inject}, as well as methods
 * annotated with {@link Initialize}. The parameters of the constructor and the types of the fields are treated as
 * component dependencies and automatically injected. The initializer methods may also have dependency parameters, but
 * those methods are only invoked after all components have been instantiated. This can be used to circumvent cyclic
 * dependencies.
 * </p>
 * <p>
 * Components are referenced by {@link Class}es, and all superclasses and superinterfaces of any given component are
 * also associated with that component. Multiple components of a given type can exist simultaneously, and a class that
 * is only associated with a single component is called a singleton. When an injectable constructor parameter or field
 * references a component, that component must be a singleton. You can use a {@link List} (or a superclass/interface
 * like {@link AbstractList} or {@link Collection}) with the component's type as the type parameter to inject multiple
 * components of the same type.
 * </p>
 * <p>
 * <b>Parameterized components are not supported</b>. A component may extend or implement a parameterized
 * superclass/interface, but the parameterized type will be ignored which may lead to a
 * {@link MultipleValidComponentsException} or a {@link ClassCastException} or other unexpected behavior. A warning
 * will be logged by default when components or their superclasses/interfaces  have parameterized types, but this
 * behavior can be disabled with {@link InjectorBuilder#disableParameterizedWarnings}.
 * </p>
 *
 * @author datafox
 */
public class Injector {
    private final Logger logger;

    private final ObjectClassMap instantiatedComponents;

    private final FunctionClassMap<PerInstanceReference<?,?>> perInstanceComponents;

    private final List<Runnable> initializers;

    private boolean building;

    /**
     * Constructor called by {@link InjectorBuilder}.
     */
    Injector(Collection<PerInstanceReference<?,?>> perInstanceComponents) {
        logger = LoggerFactory.getLogger(getClass());
        instantiatedComponents = new ObjectClassMap();
        this.perInstanceComponents = new FunctionClassMap<>(PerInstanceReference::getType);
        initializers = new ArrayList<>();

        building = true;

        instantiatedComponents.put(this);
        this.perInstanceComponents.putAll(perInstanceComponents);
    }

    /**
     * Instantiates the provided class, with the other provided class referenced as the requester for
     * {@link InstantiationDetails}. The class must either have a single constructor annotated with {@link Inject} or a
     * default constructor. Any parameters of the constructor are treated as dependencies and injected during
     * instantiation. Any non-static and non-final fields annotated with {@link Inject} are also injected directly after
     * instantiation. Finally, any methods annotated with {@link Initialize} are invoked, with their parameters also
     * treated as dependencies.
     *
     * @param type class to be instantiated
     * @param requestingClass class requesting the instance
     * @return new instance of the provided class
     *
     * @throws NoValidConstructorException if no valid constructor is present
     * @throws MultipleInjectConstructorsException if multiple constructors annotated with {@link Inject} are present
     * @throws ClassInstantiationException if a component could not be instantiated, for example if its constructor is
     * not accessible to the injector
     * @throws FieldInjectionException if a component has fields annotated with {@link Inject} that are not accessible
     * to the injector
     * @throws MethodInvocationException if a component has a method or methods annotated with {@link Initialize} that
     * are not accessible
     */
    public <T,R> T newInstance(Class<T> type, Class<R> requestingClass) {
        logger.info(InjectorStrings.instantiatingComponent(type));

        return instantiateConstructor(type, InjectorUtils.getConstructor(type, logger),
                InstantiationDetails.of(type, requestingClass));
    }

    /**
     * Calls {@link Injector#newInstance(Class, Class)} with a null requesting class.
     */
    public <T> T newInstance(Class<T> type) {
        return newInstance(type, (Class<?>) null);
    }

    /**
     * Returns all components matching the provided class. If any of those components are
     * {@link InstantiationPolicy#PER_INSTANCE}, they are instantiated and the provided requesting class will be used
     * for any {@link InstantiationDetails} dependencies.
     *
     * @param type requested component class
     * @param requestingClass class requesting the components
     * @return list of all components matching the provided class.
     *
     * @throws ClassInstantiationException if a per instance component could not be instantiated, for example if its
     * constructor is not accessible to the injector
     * @throws FieldInjectionException if a per instance component has fields annotated with {@link Inject} that are not
     * accessible to the injector
     * @throws MethodInvocationException if a per instance component has a method or methods annotated with
     * {@link Initialize} that are not accessible
     */
    @SuppressWarnings("unchecked")
    public <T,R> List<T> getComponents(Class<T> type, Class<R> requestingClass) {
        logger.info(InjectorStrings.fetchingComponents(type));

        List<T> list = new ArrayList<>();

        list.addAll(instantiatedComponents.getAndCast(type));

        list.addAll(perInstanceComponents.get(type)
                .stream()
                .map(reference -> instantiatePerInstanceComponent(
                        (PerInstanceReference<T,?>) reference,
                        InstantiationDetails.of(type, requestingClass)))
                .collect(Collectors.toList()));

        return list;
    }

    /**
     * Calls {@link Injector#getComponents(Class, Class)} with a null requesting class.
     */
    public <T> List<T> getComponents(Class<T> type) {
        return getComponents(type, null);
    }

    /**
     * Checks if only one component matches with the provided class and then calls
     * {@link Injector#getComponents(Class, Class)} and returns the first and only entry in the returned list.
     *
     * @throws UnknownComponentException if no components match the provided class
     * @throws MultipleValidComponentsException if multiple components match the provided class
     */
    public <T,R> T getSingletonComponent(Class<T> type, Class<R> requestingClass) {
        logger.info(InjectorStrings.fetchingSingleton(type));

        if(!hasComponents(type)) {
            throw LogUtils.logExceptionAndGet(logger, InjectorStrings.unknownComponent(type),
                    UnknownComponentException::new);
        }

        if(!isSingletonComponent(type)) {
            throw LogUtils.logExceptionAndGet(logger, InjectorStrings.multipleValidComponents(type),
                    MultipleValidComponentsException::new);
        }

        return getComponents(type, requestingClass).get(0);
    }


    /**
     * Calls {@link Injector#getSingletonComponent(Class, Class)} with a null requesting class.
     */
    public <T> T getSingletonComponent(Class<T> type) {
        return getSingletonComponent(type, null);
    }

    /**
     * Checks if any components are present that match with the provided class.
     *
     * @param type component class to check
     * @return true if one or more components are present that match with the provided class
     */
    public <T> boolean hasComponents(Class<T> type) {
        return instantiatedComponents.contains(type) || perInstanceComponents.contains(type);
    }

    /**
     * Checks if a single component is present that matches with the provided class.
     *
     * @param type component class to check
     * @return true if a single component is present that matches with the provided class
     */
    public <T> boolean isSingletonComponent(Class<T> type) {
        return instantiatedComponents.isSingleton(type) != perInstanceComponents.isSingleton(type);
    }

    /**
     * Registers a singleton component to be used with dependency injection. Normally only
     * used by the {@link InjectorBuilder}.
     *
     * @param instance component instance
     */
    public <T> void addComponent(T instance) {
        logger.debug(InjectorStrings.registeringComponent(instance.getClass()));
        instantiatedComponents.put(instance);
    }

    <T> T invokeMethod(Class<T> type, Method method, Object methodInstance) {
        return invokeMethod(type, method, methodInstance,
                InstantiationDetails.of(type, null));
    }

    void finishBuilding() {
        building = false;

        if(!initializers.isEmpty()) {
            logger.info(InjectorStrings.RUNNING_INITIALIZERS);
            initializers.forEach(Runnable::run);
            initializers.clear();
        }
    }

    private <T,R> T instantiateConstructor(Class<T> type, Constructor<T> constructor,
                                           InstantiationDetails<T,R> instantiationDetails) {
        logger.debug(InjectorStrings.instantiatingConstructor(constructor));

        T instance;

        try {
            constructor.trySetAccessible();
            instance = constructor.newInstance(
                    getExecutableDependencies(type, constructor, instantiationDetails));
        } catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.couldNotInstantiateConstructor(constructor),
                    e, ClassInstantiationException::new);
        }

        return initInstance(type, instance, instantiationDetails);
    }

    @SuppressWarnings("unchecked")
    private <T,O,R> T instantiatePerInstanceComponent(PerInstanceReference<T,O> reference,
                                                      InstantiationDetails<T,R> instantiationDetails) {
        logger.debug(InjectorStrings.instantiatingPerInstance(instantiationDetails));

        if(reference.getExecutable() instanceof Constructor) {
            return instantiateConstructor(reference.getType(),
                    (Constructor<T>) reference.getExecutable(), instantiationDetails);
        }

        if(reference.getExecutable() instanceof Method) {
            return invokeMethod(reference.getType(),
                    (Method) reference.getExecutable(),
                    getSingletonComponent(
                            reference.getOwner(),
                            reference.getType()),
                    instantiationDetails);
        }

        throw LogUtils.logExceptionAndGet(logger,
                InjectorStrings.executableNotConstructorOrMethod(reference),
                IllegalArgumentException::new);
    }

    @SuppressWarnings("unchecked")
    private <T,R> T invokeMethod(Class<T> type, Method method, Object methodInstance,
                                 InstantiationDetails<T,R> instantiationDetails) {
        if(methodInstance != null) {
            logger.debug(InjectorStrings.invokingMethod(method, methodInstance.getClass()));
        } else {
            logger.debug(InjectorStrings.invokingStaticMethod(method));
        }

        T instance;

        try {
            method.trySetAccessible();
            instance = (T) method.invoke(methodInstance,
                    getExecutableDependencies(type, method, instantiationDetails));
        } catch(InvocationTargetException | IllegalAccessException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.couldNotInvokeMethod(method, type),
                    e, MethodInvocationException::new);
        }

        return initInstance(type, instance, instantiationDetails);
    }

    private <T,R> Object[] getExecutableDependencies(Class<T> type, Executable executable,
                                                     InstantiationDetails<T,R> instantiationDetails) {
        if(executable instanceof Method) {
            logger.info(InjectorStrings.fetchingMethodDependencies((Method) executable, type));
        } else if(executable instanceof Constructor) {
            logger.info(InjectorStrings.fetchingConstructorDependencies((Constructor<?>) executable));
        }

        Object[] params = new Object[executable.getParameterCount()];
        for(int i=0;i< params.length;i++) {
            if(executable.getParameterTypes()[i].equals(InstantiationDetails.class)) {
                params[i] = instantiationDetails;
            } else {
                params[i] = getComponent(type,
                        executable.getParameterTypes()[i],
                        executable.getGenericParameterTypes()[i]);
            }
        }

        return params;
    }

    private <T,R> T initInstance(Class<T> type, T instance, InstantiationDetails<T,R> instantiationDetails) {
        List<Field> fields = ClassUtils.getFieldsWithAnnotation(type, Inject.class);

        if(!fields.isEmpty()) {
            logger.info(InjectorStrings.initializingFields(type));
        }

        for(Field field : fields) {
            logger.debug(InjectorStrings.initializingField(field, type));

            Object dependency;
            if(field.getType().equals(InstantiationDetails.class)) {
                dependency = instantiationDetails;
            } else {
                dependency = getComponent(type,
                        field.getType(),
                        field.getGenericType());
            }
            try {
                field.trySetAccessible();
                field.set(instance, dependency);
            } catch(IllegalAccessException e) {
                throw LogUtils.logExceptionAndGet(logger, InjectorStrings.fieldInaccessible(field, type),
                        e, FieldInjectionException::new);
            }
        }

        List<Method> methods = ClassUtils.getMethodsWithAnnotation(type, Initialize.class);

        if(!fields.isEmpty()) {
            logger.info(InjectorStrings.registeringMethods(type));
        }

        for(Method method : methods) {
            Runnable initializer = () -> {
                logger.debug(InjectorStrings.invokingMethod(method, type));
                try {
                    method.trySetAccessible();
                    method.invoke(instance,
                            getExecutableDependencies(type, method, instantiationDetails));
                } catch(IllegalAccessException | InvocationTargetException e) {
                    throw LogUtils.logExceptionAndGet(logger,
                            InjectorStrings.couldNotInvokeMethod(method, type),
                            e, MethodInvocationException::new);
                }
            };

            if(building) {
                logger.debug(InjectorStrings.methodRegistered(method, type));
                initializers.add(initializer);
            } else {
                initializer.run();
            }
        }

        return instance;
    }

    private <T> Object getComponent(Class<T> type,
                                    Class<?> dependencyType,
                                    Type parametricType) {
        logger.debug(InjectorStrings.fetchingDependency(dependencyType, type));

        if(List.class.isAssignableFrom(dependencyType)) {
            Class<?> listType = InjectorUtils.getListType(parametricType, logger);

            logger.debug(InjectorStrings.listDependency(listType));

            return getComponents(listType, type);
        }

        return getSingletonComponent(dependencyType, type);
    }

    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class PerInstanceReference<T,O> {
        private final Class<T> type;

        private final Class<O> owner;

        private final Executable executable;

        public static <T,O> PerInstanceReference<T,O> of(Class<T> type, Class<O> owner, Executable executable) {
            return new PerInstanceReference<>(type, owner, executable);
        }

        public static <T,O> PerInstanceReference<T,O> of(Class<T> type, Executable executable) {
            return new PerInstanceReference<>(type, null, executable);
        }
    }
}
