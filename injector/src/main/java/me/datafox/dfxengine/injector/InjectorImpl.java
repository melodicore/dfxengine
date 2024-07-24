package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.InstantiationDetails;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.TypeRef;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;
import me.datafox.dfxengine.injector.exception.MultipleDependenciesPresentException;
import me.datafox.dfxengine.injector.exception.NoDependenciesPresentException;
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

    private static InjectorImpl instance;

    private final Logger logger;

    private final List<ComponentData<?>> componentList;

    private final List<PrioritizedRunnable> initializerQueue;

    private final List<ComponentData<?>> voidComponentQueue;

    InjectorImpl(Stream<ComponentData<?>> components) {
        instance = this;
        logger = LoggerFactory.getLogger(InjectorImpl.class);
        componentList = new ArrayList<>();
        initializerQueue = new ArrayList<>();
        voidComponentQueue = new ArrayList<>();
        components.peek(this::instantiateOnce)
                .sorted(Comparator.comparingInt(ComponentData::getOrder))
                .forEach(componentList::add);
        runAndClearInitializers();
        runAndClearVoidComponents();
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

    /**
     * {@inheritDoc} An exception is also thrown if multiple Components or no Components are present.
     *
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     *
     * @throws ParameterCountMismatchException {@inheritDoc}
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
     * {@inheritDoc} If multiple Components or no Components are present, an exception is thrown.
     *
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * {@inheritDoc} An exception is also thrown if multiple Components or no Components are present.
     *
     * @param type {@inheritDoc}
     * @param requesting {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param <R> {@inheritDoc}
     *
     * @throws ParameterCountMismatchException {@inheritDoc}
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
     * {@inheritDoc} If multiple Components or no Components are present, an exception is thrown.
     *
     * @param type {@inheritDoc}
     * @param requesting {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param <R> {@inheritDoc}
     *
     * @throws MultipleDependenciesPresentException if multiple {@link Component Components} with the specified type are
     * present
     * @throws NoDependenciesPresentException if no {@link Component Components} with the specified type are present
     */
    @Override
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
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     *
     * @throws ParameterCountMismatchException {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     * @return {@link List} {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> List<T> getComponents(TypeRef<T> type) {
        return getComponents(type, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     * @param requesting {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param <R> {@inheritDoc}
     *
     * @throws ParameterCountMismatchException {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     * @param requesting {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param <R> {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T,R> List<T> getComponents(TypeRef<T> type, TypeRef<R> requesting) {
        return getComponent(TypeRef.of(List.class, type), requesting);
    }

    private <T> ClassReference<T> getReference(TypeRef<T> type) {
        if(type == null) {
            return null;
        }
        ClassReference<T> reference = ClassReference
                .<T>builder()
                .type(type.getType())
                .parameters(type
                        .getParameters()
                        .stream()
                        .map(this::getReference)
                        .collect(Collectors.toList()))
                .build();
        if(List.class.equals(type.getType())) {
            reference.setList(true);
            reference.setListReference(reference.getParameters().get(0));
        }
        return reference;
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

    public enum LoggerType {
        CLASSIC,
        PARAMETERS,
        FULL_PARAMETERS
    }
}
