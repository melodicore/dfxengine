package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;
import me.datafox.dfxengine.injector.exception.MultipleDependenciesPresentException;
import me.datafox.dfxengine.injector.exception.NoDependenciesPresentException;
import me.datafox.dfxengine.injector.exception.ParametricEventWithoutInterfaceException;
import me.datafox.dfxengine.injector.exception.UnresolvedOrUnknownTypeException;
import me.datafox.dfxengine.injector.internal.*;
import me.datafox.dfxengine.injector.utils.InjectorStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.multipleDependenciesRuntime;
import static me.datafox.dfxengine.injector.utils.InjectorStrings.noDependenciesRuntime;

/**
 * <p>
 * A dependency injector. For general information, see
 * <a href="https://github.com/melodicore/dfxengine/blob/master/injector/README.md">README.md</a> on GitHub. Use
 * {@link InjectorBuilder} to build.
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
public class InjectorImpl implements Injector {
    private static InjectorImpl instance;

    @Component
    private static InjectorImpl getInstance() {
        return instance;
    }

    @Component(order = Integer.MAX_VALUE)
    private static LoggerType getLoggerType() {
        return LoggerType.PARAMETERS;
    }

    @Component(value = InstantiationPolicy.PER_INSTANCE, order = Integer.MAX_VALUE)
    private static Logger getLogger(LoggerType type, InstantiationDetails details) {
        if(details.getRequesting() == null) {
            return LoggerFactory.getLogger(Object.class);
        }
        switch(type) {
            case CLASSIC:
                return LoggerFactory.getLogger(details.getRequesting().getType());
            case PARAMETERS:
                return LoggerFactory.getLogger(details.getRequesting().toStringParametersWithoutPackage());
            case FULL_PARAMETERS:
                return LoggerFactory.getLogger(details.getRequesting().toString());
        }
        return LoggerFactory.getLogger(details.getRequesting().getType());
    }

    private final Logger logger;

    private final ComponentDataFactory factory;

    private final List<ComponentData<?>> componentList;

    private final List<EventData<?>> eventList;

    private final List<PrioritizedRunnable> initializerQueue;

    private final List<ComponentData<?>> voidComponentQueue;

    private final List<Object> eventQueue;

    /**
     * Package-private constructor for {@link InjectorImpl}.
     *
     * @param factory {@link ComponentDataFactory} for this injector
     * @param components {@link Stream} of all {@link ComponentData} for this injector
     * @param eventData {@link List} of all {@link EventData} for this injector
     */
    InjectorImpl(ComponentDataFactory factory, Stream<ComponentData<?>> components, List<EventData<?>> eventData) {
        instance = this;
        logger = LoggerFactory.getLogger(InjectorImpl.class);
        this.factory = factory;
        componentList = new ArrayList<>();
        eventList = eventData;
        initializerQueue = new ArrayList<>();
        voidComponentQueue = new ArrayList<>();
        eventQueue = new ArrayList<>();
        components.peek(this::instantiateOnce)
                .sorted(Comparator.comparingInt(ComponentData::getOrder))
                .forEach(componentList::add);
        runAndClearInitializers();
        runAndClearVoidComponents();
    }

    /**
     * Returns a single {@link Component} of the specified type. If the {@link Class} has type parameters, use
     * {@link #getComponent(TypeRef)} instead. Otherwise, an exception is thrown. An exception is also thrown if
     * multiple components or no components are present.
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
    @Override
    public <T> T getComponent(Class<T> type) {
        try {
            return getComponent(TypeRef.of(type));
        } catch(ParameterCountMismatchException e) {
            throw LogUtils.logExceptionAndGet(logger, e.getMessage(), ParameterCountMismatchException::new);
        }
    }

    /**
     * Returns a single {@link Component} of the specified type. If multiple components or no components are present,
     * an exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     *
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    @Override
    public <T> T getComponent(TypeRef<T> type) {
        return getComponent(type, null);
    }

    /**
     * Returns a single {@link Component} of the specified type. If the component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails}. If the {@link Class} has type parameters, use
     * {@link #getComponent(TypeRef)} instead. An exception is also thrown if multiple components or no components are
     * present.
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
    @Override
    public <T,R> T getComponent(Class<T> type, Class<R> requesting) {
        try {
            return getComponent(TypeRef.of(type), TypeRef.of(requesting));
        } catch(ParameterCountMismatchException e) {
            throw LogUtils.logExceptionAndGet(logger, e.getMessage(), ParameterCountMismatchException::new);
        }
    }

    /**
     * Returns a single {@link Component} of the specified type. If the component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails}. If multiple components or no components are present, an
     * exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @param requesting type of the object requesting the {@link Component}
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     * @param <R> type of the object requesting the {@link Component}
     *
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    @Override
    public <T,R> T getComponent(TypeRef<T> type, TypeRef<R> requesting) {
        ClassReference<T> reference = factory.buildClasReferenceFromTypeRef(type);
        ClassReference<R> requestingReference = factory.buildClasReferenceFromTypeRef(requesting);
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
    @Override
    public <T> List<T> getComponents(Class<T> type) {
        try {
            return getComponents(TypeRef.of(type));
        } catch(ParameterCountMismatchException e) {
            throw LogUtils.logExceptionAndGet(logger, e.getMessage(), ParameterCountMismatchException::new);
        }
    }

    /**
     * Returns {@link Component Components} of the specified type.
     *
     * @param type type of the {@link Component Components} to be requested
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     */
    @Override
    public <T> List<T> getComponents(TypeRef<T> type) {
        return getComponents(type, null);
    }

    /**
     * Returns {@link Component Components} of the specified type. If any of the Components have
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails}. If the {@link Class} has type parameters, use
     * {@link #getComponent(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link Class} has type parameters
     */
    @Override
    public <T,R> List<T> getComponents(Class<T> type, Class<R> requesting) {
        try {
            return getComponents(TypeRef.of(type), TypeRef.of(requesting));
        } catch(ParameterCountMismatchException e) {
            throw LogUtils.logExceptionAndGet(logger, e.getMessage(), ParameterCountMismatchException::new);
        }
    }

    /**
     * Returns {@link Component Components} of the specified type. If any of the components have
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails}.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T,R> List<T> getComponents(TypeRef<T> type, TypeRef<R> requesting) {
        return getComponent(TypeRef.of(List.class, type), requesting);
    }

    /**
     * Calls all {@link EventHandler EventHandlers} with the specified event that can accept its type as a parameter.
     * If the event has type parameters, it must implement {@link ParametricEvent}.
     *
     * @param event event to be invoked
     * @param <T> type of the event
     */
    @Override
    public <T> void invokeEvent(T event) {
        invokeEventInternal(event);
        while(!eventQueue.isEmpty()) {
            List<Object> copy = new ArrayList<>(eventQueue);
            eventQueue.clear();
            copy.forEach(this::invokeEventInternal);
        }
    }

    /**
     * Calls all {@link EventHandler EventHandlers} with the specified events that can accept their types as parameters.
     * If an event has type parameters, it must implement {@link ParametricEvent}.
     *
     * @param events events to be invoked
     */
    @Override
    public void invokeEvents(Collection<?> events) {
        events.forEach(this::invokeEventInternal);
        while(!eventQueue.isEmpty()) {
            List<Object> copy = new ArrayList<>(eventQueue);
            eventQueue.clear();
            copy.forEach(this::invokeEventInternal);
        }
    }

    private void runAndClearInitializers() {
        initializerQueue.stream().sorted().forEach(Runnable::run);
        initializerQueue.clear();
    }

    private void runAndClearVoidComponents() {
        voidComponentQueue
                .stream()
                .sorted(Comparator.comparingInt(ComponentData::getOrder))
                .forEach(this::invokeVoid);
        voidComponentQueue.clear();
    }

    @SuppressWarnings("unchecked")
    private <T> void invokeEventInternal(T event) {
        TypeRef<T> eventType;
        if(event instanceof ParametricEvent) {
            eventType = (TypeRef<T>) ((ParametricEvent) event).getType();
        } else {
            eventType = (TypeRef<T>) TypeRef.of(event.getClass());
        }

        ClassReference<T> eventReference;
        try {
            eventReference = factory.buildClasReferenceFromTypeRef(eventType);
        } catch(UnresolvedOrUnknownTypeException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.parametricEventWithoutInterface(event),
                    ParametricEventWithoutInterfaceException::new);
        }

        List<EventData<T>> events = eventList
                .stream()
                .filter(e -> e.getEvent().isAssignableFrom(eventReference))
                .map(e -> (EventData<T>) e)
                .collect(Collectors.toList());

        events.forEach(e -> invokeEvent(e, event));
    }

    private <T> void invokeEvent(EventData<T> eventData, T event) {
        if(eventData.getOwner() != null) {
            eventQueue.addAll(componentList.stream()
                    .filter(data -> eventData.getOwner()
                            .isAssignableFrom(data.getReference().getActualReference()))
                    .filter(data -> InstantiationPolicy.ONCE.equals(data.getPolicy()))
                    .map(ComponentData::getObjects)
                    .flatMap(List::stream)
                    .map(obj -> {
                        try {
                            eventData.getMethod().trySetAccessible();
                            return eventData.getMethod().invoke(obj, event);
                        } catch(IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .flatMap(Stream::ofNullable)
                    .collect(Collectors.toList()));
        } else {
            Object returned;
            try {
                eventData.getMethod().trySetAccessible();
                returned = eventData.getMethod().invoke(null, event);
            } catch(IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if(returned != null) {
                eventQueue.add(returned);
            }
        }
    }

    private <T> void instantiateOnce(ComponentData<T> data) {
        if(!data.getPolicy().equals(InstantiationPolicy.ONCE)) {
            return;
        }
        if(data.getReference() == null) {
            voidComponentQueue.add(data);
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

    private void invokeVoid(ComponentData<?> data) {
        Object[] parameters = new Object[0];
        List<ClassReference<?>> requesting = new ArrayList<>();
        ClassReference<Void> voidReference = ClassReference.voidType();
        requesting.add(voidReference);
        if(!data.getParameters().isEmpty()) {
            parameters = data
                    .getParameters()
                    .stream()
                    .map(reference -> getParameter(reference,
                            voidReference,
                            requesting,
                            data.getDependencies()))
                    .toArray(Object[]::new);
        }
        try {
            data.getExecutable().setAccessible(true);
            Method method = ((Method) data.getExecutable());
            Object owner = null;
            if(data.getOwner() != null) {
                owner = getParameter(data.getOwner(),
                        voidReference,
                        requesting,
                        data.getDependencies());
            }
            method.invoke(owner, parameters);
        } catch(InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        if(InstantiationDetails.class.equals(reference.getActualReference().getType())) {
            InstantiationDetails details = InstantiationDetails
                    .builder()
                    .type(instantiating.toTypeRef())
                    .requesting(requesting.size() < 2 ? null : requesting.get(requesting.size() - 2).toTypeRef())
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

    /**
     * Modes for naming a {@link Logger} when requested as a dependency.
     */
    public enum LoggerType {
        /**
         * Uses the base {@link Class} of the {@link Component} requesting the {@link Logger} as a dependency with no
         * type parameters.
         */
        CLASSIC,

        /**
         * Uses the signature of the {@link Component} requesting the {@link Logger} as a dependency with simple names
         * (no package names) for type parameters.
         */
        PARAMETERS,

        /**
         * Uses the signature of the {@link Component} requesting the {@link Logger} as a dependency with type
         * parameters, including package names.
         */
        FULL_PARAMETERS
    }
}
