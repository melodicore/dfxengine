package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.exception.ComponentWithMultipleOptionsForSingletonDependency;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.internal.InitializeReference;
import me.datafox.dfxengine.injector.internal.PrioritizedRunnable;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
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

    private static Injector instance;

    private final List<ComponentData<?>> componentList;

    private final List<PrioritizedRunnable> initializerQueue;

    Injector(Stream<ComponentData<?>> components) {
        instance = this;
        componentList = new ArrayList<>();
        initializerQueue = new ArrayList<>();
        components.peek(this::instantiateOnce).forEach(componentList::add);
        initializerQueue.stream().sorted().forEach(Runnable::run);
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
        return getParameter(reference, reference, requestingReference, List.of(componentList));
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
        data.setObjects(instantiate(data, null));
    }

    @SuppressWarnings("unchecked")
    private <T,R> List<T> instantiate(ComponentData<T> data, ClassReference<R> requesting) {
        Object[] parameters = new Object[0];
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
                                .delegate(() -> initialize(init, object, requesting))
                                .build())).collect(Collectors.toList()));
        return list;
    }

    private <T, R> void initialize(InitializeReference<?> init, T object, ClassReference<R> requesting) {
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

    private <T,R> void setFields(ComponentData<T> data, T object, ClassReference<R> requesting) {
        data.getFields().forEach(field -> setField(data, field.getField(), field.getReference(), object, requesting));
    }

    private <C,T,R> void setField(ComponentData<C> data, Field field, ClassReference<T> reference, C object, ClassReference<R> requesting) {
        try {
            field.setAccessible(true);
            T parameter = getParameter(reference, data.getReference(), requesting, data.getDependencies());
            field.set(object, parameter);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T,I,R> T getParameter(ClassReference<T> reference, ClassReference<I> instantiating, ClassReference<R> requesting, List<List<ComponentData<?>>> components) {
        if(InstantiationDetails.class.equals(reference.getActualReference().getType())) {
            InstantiationDetails details = InstantiationDetails
                    .builder()
                    .type(instantiating.getType())
                    .parameters(instantiating.getParameters())
                    .requestingType(requesting != null ? requesting.getType() : null)
                    .requestingParameters(requesting != null ? requesting.getParameters() : List.of())
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
                .filter(data -> reference.getActualReference()
                        .isAssignableFrom(data.getReference().getActualReference()))
                .map(data -> getObjects(data, requesting))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(reference.isList()) {
            return (T) list;
        } else {
            if(list.size() != 1) {
                list = components
                        .stream()
                        .flatMap(List::stream)
                        .filter(Predicate.not(ComponentData::isDefaultImpl))
                        .filter(data -> reference.getActualReference()
                                .isAssignableFrom(data.getReference().getActualReference()))
                        .map(ComponentData::getObjects)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                if(list.size() != 1) {
                    throw new ComponentWithMultipleOptionsForSingletonDependency("");
                }
            }
            return (T) list.get(0);
        }
    }

    private <T,R> List<T> getObjects(ComponentData<T> data, ClassReference<R> requesting) {
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
