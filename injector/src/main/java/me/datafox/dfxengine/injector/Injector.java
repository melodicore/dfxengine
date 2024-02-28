package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.internal.InitializeReference;
import me.datafox.dfxengine.injector.internal.PrioritizedRunnable;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.*;

/**
 * <p>
 * A dependency injector. For general information, see
 * <a href="https://github.com/melodicore/dfxengine/blob/master/injector/README.md">README.md</a> on GitHub. Use
 * {@link #builder()} to build.
 * </p>
 * <p>
 * {@link #getComponent(Class)}, {@link #getComponent(TypeRef)}, {@link #getComponent(Class, Class)},
 * {@link #getComponent(TypeRef, TypeRef)}, {@link #getComponents(Class)}, {@link #getComponents(TypeRef)},
 * {@link #getComponents(Class, Class)} and {@link #getComponents(TypeRef, TypeRef)} can be used to retrieve components.
 * The {@code requesting} parameter is only relevant when requesting components that have
 * {@link InstantiationPolicy#PER_INSTANCE}.
 * </p>
 *
 * @author datafox
 */
public class Injector {
    /**
     * @return {@link InjectorBuilder} instance
     */
    public static InjectorBuilder builder() {
        return new InjectorBuilder();
    }

    @Component
    private static Injector getInstance() {
        return instance;
    }

    @Component(value = InstantiationPolicy.PER_INSTANCE, defaultImpl = true)
    private static Logger getLogger(InstantiationDetails details) {
        if(details.getRequesting() == null) {
            return LoggerFactory.getLogger(Object.class);
        }
        return LoggerFactory.getLogger(details.getRequesting().getType());
    }

    private static Injector instance;

    private final Logger logger;

    private final List<ComponentData<?>> componentList;

    private final List<PrioritizedRunnable> initializerQueue;

    Injector(Stream<ComponentData<?>> components) {
        instance = this;
        logger = LoggerFactory.getLogger(Injector.class);
        componentList = new ArrayList<>();
        initializerQueue = new ArrayList<>();
        components.peek(this::instantiateOnce)
                .sorted(Comparator.comparingInt(ComponentData::getOrder))
                .forEach(componentList::add);
        runAndClearInitializers();
    }

    private void runAndClearInitializers() {
        initializerQueue.stream().sorted().forEach(Runnable::run);
        initializerQueue.clear();
    }

    /**
     * Returns a single {@link Component} of the specified type. If the {@link Class} has type parameters, use
     * {@link #getComponent(TypeRef)} instead. Otherwise, or if multiple Components or no Components are present, an
     * exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link Class} has type parameters
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    public <T> T getComponent(Class<T> type) {
        return getComponent(TypeRef.of(type));
    }

    /**
     * Returns a single {@link Component} of the specified type. If multiple Components or no Components are present, an
     * exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link TypeRef#getType()} has a different amount of type
     * parameters than {@link TypeRef#getParameters()}
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    public <T> T getComponent(TypeRef<T> type) {
        return getComponent(type, null);
    }

    /**
     * Returns a single {@link Component} of the specified type. If the Component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. If the {@link Class} has type
     * parameters, use {@link #getComponent(TypeRef)} instead. Otherwise, or if multiple Components or no Components are
     * present, an exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @param requesting type of the object requesting the {@link Component}
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     * @param <R> type of the object requesting the {@link Component}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link Class} has type parameters
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    public <T,R> T getComponent(Class<T> type, Class<R> requesting) {
        return getComponent(TypeRef.of(type), TypeRef.of(requesting));
    }

    /**
     * Returns a single {@link Component} of the specified type. If the Component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. If multiple Components or no Components
     * are present, an exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @param requesting type of the object requesting the {@link Component}
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     * @param <R> type of the object requesting the {@link Component}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link TypeRef#getType()} has a different
     * amount of type parameters than {@link TypeRef#getParameters()}
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    public <T,R> T getComponent(TypeRef<T> type, TypeRef<R> requesting) {
        ClassReference<T> reference = getReference(type);
        ClassReference<R> requestingReference = getReference(requesting);
        List<ClassReference<?>> referenceStack = new ArrayList<>();
        if(requestingReference != null) {
            referenceStack.add(requestingReference);
        }
        T object = getParameter(reference, reference, referenceStack, List.of(componentList));
        runAndClearInitializers();
        return object;
    }

    /**
     * Returns {@link Component Components} of the specified type. If the {@link Class} has type parameters, use
     * {@link #getComponents(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component Components} to be requested
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link Class} has type parameters
     */
    public <T> List<T> getComponents(Class<T> type) {
        return getComponents(TypeRef.of(type));
    }

    /**
     * Returns {@link Component Components} of the specified type.
     *
     * @param type type of the {@link Component Components} to be requested
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link TypeRef#getType()} has a different amount of type
     * parameters than {@link TypeRef#getParameters()}
     */
    public <T> List<T> getComponents(TypeRef<T> type) {
        return getComponents(type, null);
    }

    /**
     * Returns {@link Component Components} of the specified type. If any of the Components have
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. If the {@link Class} has type
     * parameters, use {@link #getComponent(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link Class} has type parameters
     */
    public <T,R> List<T> getComponents(Class<T> type, Class<R> requesting) {
        return getComponents(TypeRef.of(type), TypeRef.of(requesting));
    }

    /**
     * Returns {@link Component Components} of the specified type. If any of the Components hav
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link TypeRef#getType()} has a different
     * amount of type parameters than {@link TypeRef#getParameters()}
     */
    @SuppressWarnings("unchecked")
    public <T,R> List<T> getComponents(TypeRef<T> type, TypeRef<R> requesting) {
        return getComponent(TypeRef.of(List.class, type), requesting);
    }

    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> getReference(TypeRef<T> type) {
        if(type == null) {
            return null;
        }
        ClassReference<T> reference = ClassReference
                .<T>builder()
                .typeRef(type)
                .build();
        if(List.class.equals(type.getType())) {
            reference.setList(true);
            reference.setListReference(ClassReference
                    .builder()
                    .typeRef((TypeRef<Object>) type.getParameters().get(0))
                    .build());
        }
        return reference;
    }

    private <T> void instantiateOnce(ComponentData<T> data) {
        if(!data.getPolicy().equals(InstantiationPolicy.ONCE)) {
            return;
        }
        List<ClassReference<?>> referenceStack = new ArrayList<>();
        data.setObjects(instantiate(data, referenceStack));
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> instantiate(ComponentData<T> data, List<ClassReference<?>> requesting) {
        Object[] parameters = new Object[0];
        requesting.add(data.getReference().getActualReference());
        if(!data.getParameters().isEmpty()) {
            parameters = data
                    .getParameters()
                    .stream()
                    .map(reference -> getParameter(reference,
                            data.getReference().getActualReference(),
                            requesting,
                            data.getDependencies()))
                    .toArray(Object[]::new);
        }
        List<T> list;
        try {
            data.getExecutable().setAccessible(true);
            if(data.getExecutable() instanceof Constructor) {
                list = List.of(((Constructor<T>) data.getExecutable()).newInstance(parameters));
            } else {
                Method method = ((Method) data.getExecutable());
                Object owner = null;
                if(data.getOwner() != null) {
                    owner = getParameter(data.getOwner(),
                            data.getReference().getActualReference(),
                            requesting,
                            data.getDependencies());
                }
                if(data.getReference().isList()) {
                    list = (List<T>) method.invoke(owner, parameters);
                } else {
                    list = List.of((T) method.invoke(owner, parameters));
                }
            }
        } catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        list.forEach(object -> setFields(data, object, requesting));
        initializerQueue.addAll(list
                .stream()
                .flatMap(object -> data
                        .getInitializers()
                        .stream()
                        .map(init -> PrioritizedRunnable
                                .builder()
                                .priority(init.getPriority())
                                .delegate(() -> initialize(init, object, new ArrayList<>(requesting)))
                                .build())).collect(Collectors.toList()));
        requesting.remove(requesting.size() - 1);
        return list;
    }

    private <T> void initialize(InitializeReference<?> init, T object, List<ClassReference<?>> requesting) {
        try {
            init.getMethod().setAccessible(true);
            init.getMethod().invoke(object, init
                    .getParameters()
                    .stream()
                    .map(reference -> getParameter(reference,
                            init.getReference().getActualReference(),
                            requesting, List.of(componentList)))
                    .toArray(Object[]::new));
        } catch(IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void setFields(ComponentData<T> data, T object, List<ClassReference<?>> requesting) {
        data.getFields().forEach(field -> setField(data, field.getField(), field.getReference(), object, requesting));
    }

    private <C,T> void setField(ComponentData<C> data, Field field, ClassReference<T> reference, C object, List<ClassReference<?>> requesting) {
        try {
            field.setAccessible(true);
            T parameter = getParameter(reference, data.getReference(), requesting, data.getDependencies());
            field.set(object, parameter);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T,I> T getParameter(ClassReference<T> reference, ClassReference<I> instantiating, List<ClassReference<?>> requesting, List<List<ComponentData<?>>> components) {
        if(InstantiationDetails.class.equals(reference.getActualReference().getTypeRef().getType())) {
            InstantiationDetails details = InstantiationDetails
                    .builder()
                    .type(instantiating.getTypeRef())
                    .requesting(requesting.size() < 2 ? null : requesting.get(requesting.size() - 2).getTypeRef())
                    .build();
            if(reference.isList()) {
                return (T) List.of(details);
            } else {
                return (T) details;
            }
        }
        List<ComponentData<?>> componentList = components
                .stream()
                .flatMap(List::stream)
                .filter(data -> reference.getActualReference()
                        .isAssignableFrom(data.getReference().getActualReference()))
                .distinct()
                .sorted(Comparator.comparingInt(ComponentData::getOrder))
                .collect(Collectors.toList());
        List<?> list = componentList
                .stream()
                .map(data -> getObjects(data, requesting))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(reference.isList()) {
            return (T) list;
        } else {
            if(list.size() != 1) {
                list = componentList.stream()
                        .filter(Predicate.not(ComponentData::isDefaultImpl))
                        .map(data -> getObjects(data, requesting))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                if(list.size() != 1) {
                    if(componentList.size() > 1 && componentList.get(0).getOrder() != componentList.get(1).getOrder()) {
                        list = getObjects(componentList.get(0), requesting);
                    }
                    if(list.isEmpty()) {
                        throw LogUtils.logExceptionAndGet(logger,
                                noDependenciesRuntime(reference),
                                NoDependenciesPresentException::new);
                    }
                    if(list.size() > 1) {
                        throw LogUtils.logExceptionAndGet(logger,
                                multipleDependenciesRuntime(reference),
                                MultipleDependenciesPresentException::new);
                    }
                }
            }
            return (T) list.get(0);
        }
    }

    private <T> List<T> getObjects(ComponentData<T> data, List<ClassReference<?>> requesting) {
        if(data.getPolicy().equals(InstantiationPolicy.ONCE)) {
            return data.getObjects();
        } else {
            return instantiate(data, requesting);
        }
    }
}
