package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.exception.ComponentWithMultipleOptionsForSingletonDependency;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.internal.InitializeReference;
import me.datafox.dfxengine.injector.internal.PrioritizedRunnable;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class Injector {
    @Component
    private static Injector getInstance() {
        return instance;
    }

    @Component(value = InstantiationPolicy.PER_INSTANCE, defaultImpl = true)
    private static Logger getLogger(InstantiationDetails details) {
        if(details.getRequestingType() == null) {
            return LoggerFactory.getLogger(Object.class);
        }
        return LoggerFactory.getLogger(details.getRequestingType());
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

    public <T> T getComponent(Class<T> type) {
        return getComponent(type, List.of());
    }

    public <T> T getComponent(Class<T> type, List<Parameter<?>> parameters) {
        return getComponent(type, parameters, null, null);
    }

    public <T,R> T getComponent(Class<T> type, Class<R> requesting) {
        return getComponent(type, List.of(), requesting, List.of());
    }

    public <T,R> T getComponent(Class<T> type, List<Parameter<?>> parameters, Class<R> requesting) {
        return getComponent(type, parameters, requesting, List.of());
    }

    public <T,R> T getComponent(Class<T> type, Class<R> requesting, List<Parameter<?>> requestingParameters) {
        return getComponent(type, List.of(), requesting, requestingParameters);
    }

    public <T,R> T getComponent(Class<T> type, List<Parameter<?>> parameters, Class<R> requesting, List<Parameter<?>> requestingParameters) {
        checkParameterCount(type, parameters);
        if(requesting != null) {
            checkParameterCount(requesting, requestingParameters);
        }
        ClassReference<T> reference = getReference(type, parameters);
        ClassReference<R> requestingReference = getReference(requesting, requestingParameters);
        Stack<ClassReference<?>> referenceStack = new Stack<>();
        if(requestingReference != null) {
            referenceStack.push(requestingReference);
        }
        T object = getParameter(reference, reference, referenceStack, List.of(componentList));
        runAndClearInitializers();
        return object;
    }

    public <T> List<T> getComponents(Class<T> type) {
        return getComponents(type, List.of());
    }

    public <T> List<T> getComponents(Class<T> type, List<Parameter<?>> parameters) {
        return getComponents(type, parameters, null, null);
    }

    public <T,R> List<T> getComponents(Class<T> type, Class<R> requesting) {
        return getComponents(type, List.of(), requesting, List.of());
    }

    public <T,R> List<T> getComponents(Class<T> type, List<Parameter<?>> parameters, Class<R> requesting) {
        return getComponents(type, parameters, requesting, List.of());
    }

    public <T,R> List<T> getComponents(Class<T> type, Class<R> requesting, List<Parameter<?>> requestingParameters) {
        return getComponents(type, List.of(), requesting, requestingParameters);
    }

    @SuppressWarnings("unchecked")
    public <T,R> List<T> getComponents(Class<T> type, List<Parameter<?>> parameters, Class<R> requesting, List<Parameter<?>> requestingParameters) {
        return getComponent(List.class,Parameter.listOf(type, parameters), requesting, requestingParameters);
    }

    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> getReference(Class<T> type, List<Parameter<?>> parameters) {
        if(type == null) {
            return null;
        }
        ClassReference<T> reference = ClassReference
                .<T>builder()
                .type(type)
                .parameters(parameters)
                .build();
        if(List.class.equals(type)) {
            reference.setList(true);
            reference.setListReference(ClassReference
                    .builder()
                    .type((Class<Object>) parameters.get(0).getType())
                    .parameters(parameters.get(0).getParameters())
                    .build());
        }
        return reference;
    }

    private <T> void checkParameterCount(Class<T> type, List<Parameter<?>> parameters) {
        if(Array.class.equals(type)) {
            if(parameters.size() != 1) {
                throw new IllegalArgumentException();
            }
            return;
        }
        if(type.getTypeParameters().length != parameters.size()) {
            throw new IllegalArgumentException();
        }
        parameters.forEach(param -> checkParameterCount(param.getType(), param.getParameters()));
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
        if(InstantiationDetails.class.equals(reference.getActualReference().getType())) {
            InstantiationDetails details = InstantiationDetails
                    .builder()
                    .type(instantiating.getType())
                    .parameters(instantiating.getParameters())
                    .requestingType(requesting.size() < 2 ? null : requesting.get(requesting.size() - 2).getType())
                    .requestingParameters(requesting.size() < 2 ? List.of() : requesting.get(requesting.size() - 2).getParameters())
                    .build();
            if(reference.isList()) {
                return (T) List.of(details);
            } else {
                return (T) details;
            }
        }
        List<?> list = components
                .stream()
                .flatMap(List::stream)
                .distinct()
                .filter(data -> reference.getActualReference()
                        .isAssignableFrom(data.getReference().getActualReference()))
                .map(data -> getObjects(data, requesting))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(reference.isList()) {
            return (T) list;
        } else {
            if(list.size() != 1) {
                List<ComponentData<?>> componentList = components
                        .stream()
                        .flatMap(List::stream)
                        .filter(Predicate.not(ComponentData::isDefaultImpl))
                        .filter(data -> reference.getActualReference()
                                .isAssignableFrom(data.getReference().getActualReference()))
                        .sorted(Comparator.comparingInt(ComponentData::getOrder))
                        .collect(Collectors.toList());
                list = componentList.stream()
                        .map(data -> getObjects(data, requesting))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                if(list.size() != 1) {
                    if(componentList.size() > 1 && componentList.get(0).getOrder() != componentList.get(1).getOrder()) {
                        list = getObjects(componentList.get(0), requesting);
                    }
                    if(list.size() != 1) {
                        throw LogUtils.logExceptionAndGet(logger,
                                "Singleton component requested but multiple are present",
                                ComponentWithMultipleOptionsForSingletonDependency::new);
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

    public static InjectorBuilder builder() {
        return new InjectorBuilder();
    }
}
