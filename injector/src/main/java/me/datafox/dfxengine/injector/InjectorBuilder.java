package me.datafox.dfxengine.injector;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ClassReferenceFactory;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.internal.FieldReference;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class InjectorBuilder {
    private final Logger logger;
    private final ClassGraph classGraph;

    InjectorBuilder() {
        logger = LoggerFactory.getLogger(InjectorBuilder.class);
        classGraph = new ClassGraph();
    }

    public Injector build() {
        ScanResult scan = classGraph.enableAllInfo().enableSystemJarsAndModules().scan();

        ClassInfoList componentClasses = scan.getClassesWithAnnotation(Component.class);
        ClassInfoList componentMethodClasses = scan.getClassesWithMethodAnnotation(Component.class);
        ClassInfoList instantiatedComponentMethodClasses =
                componentMethodClasses.filter(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(Component.class))
                        .anyMatch(Predicate.not(MethodInfo::isStatic)));
        instantiatedComponentMethodClasses.removeAll(componentClasses);
        ClassInfoList instantiatedClasses = new ClassInfoList(componentClasses);
        instantiatedClasses.addAll(instantiatedComponentMethodClasses);
        Map<ClassInfo,FieldInfoList> injectFieldMap = scan
                .getClassesWithFieldAnnotation(Inject.class)
                .stream()
                .filter(instantiatedClasses::contains)
                .collect(Collectors.toMap(Function.identity(), info -> info
                        .getDeclaredFieldInfo()
                        .stream()
                        .filter(field -> field.hasAnnotation(Inject.class))
                        .collect(Collectors.toCollection(FieldInfoList::new))));

        MethodInfoList executables = instantiatedClasses
                .stream()
                .map(this::getValidConstructor)
                .collect(Collectors.toCollection(MethodInfoList::new));

        executables.addAll(componentMethodClasses
                .stream()
                .flatMap(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(Component.class)))
                .collect(Collectors.toList()));
        ClassReferenceFactory factory = new ClassReferenceFactory(scan.getAllClassesAsMap(), injectFieldMap);
        List<ComponentData<?>> components = executables
                .stream()
                .map(factory::buildComponentData)
                .collect(Collectors.toList());
        scan.close();
        parseDependencies(components);
        checkCyclicDependencies(components);
        Map<ComponentData<?>,Integer> orderMap = new HashMap<>(getOrder(components));

        return new Injector(components.stream()
                .sorted(Comparator.comparing(orderMap::get)));
    }

    private MethodInfo getValidConstructor(ClassInfo info) {
        MethodInfoList list = info.getDeclaredConstructorInfo().filter(method ->
                method.getParameterInfo().length == 0 ||
                        method.hasAnnotation(Inject.class));

        if(list.isEmpty()) {
            throw LogUtils.logExceptionAndGet(logger,
                    "Class " + info.loadClass() + " has no valid constructors and cannot be instantiated",
                    ComponentClassWithNoValidConstructorsException::new);
        }
        if(list.size() > 1) {
            throw LogUtils.logExceptionAndGet(logger,
                    "Class " + info.loadClass() + " has multiple valid constructors " + list + " and cannot be instantiated",
                    ComponentClassWithMultipleValidConstructorsException::new);
        }
        return list.get(0);
    }

    private void parseDependencies(List<ComponentData<?>> components) {
        for(ComponentData<?> component : components) {
            component.setDependencies(component
                    .getParameters()
                    .stream()
                    .map(ClassReference::getActualReference)
                    .map(data -> parseDependency(data, components))
                    .collect(Collectors.toCollection(ArrayList::new)));
            if(component.getOwner() != null) {
                List<ComponentData<?>> owners = components
                        .stream()
                        .filter(data -> component.getOwner().equals(data.getReference()))
                        .collect(Collectors.toList());
                component.getDependencies().add(owners);
            }
            component.getDependencies().addAll(component
                    .getFields()
                    .stream()
                    .map(FieldReference::getReference)
                    .map(ClassReference::getActualReference)
                    .map(data -> parseDependency(data, components))
                    .collect(Collectors.toList()));
            checkDependencies(component);
        }
    }

    private List<ComponentData<?>> parseDependency(ClassReference<?> classReference, List<ComponentData<?>> components) {
        Class<?> type = classReference.getActualReference().getType();
        return components
                .stream()
                .filter(data -> classReference.isAssignableFrom(data.getReference().getActualReference()))
                .collect(Collectors.toList());
    }

    private void checkDependencies(ComponentData<?> data) {
        int expectedSize = data.getParameters().size() + data.getFields().size();
        if(data.getOwner() != null) {
            expectedSize++;
        }
        if(expectedSize != data.getDependencies().size()) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < expectedSize; i++) {
            ClassReference<?> reference;
            if(i < data.getParameters().size()) {
                reference = data.getParameters().get(i);
            } else if(i == data.getParameters().size() && data.getOwner() != null) {
                reference = data.getOwner();
            } else {
                int index = i - data.getParameters().size();
                if(data.getOwner() != null) {
                    index--;
                }
                reference = data.getFields().get(index).getReference();
            }
            if(InstantiationDetails.class.equals(reference.getActualReference().getType())) {
                continue;
            }
            List<ComponentData<?>> dependency = data.getDependencies().get(i);
            if(dependency.isEmpty()) {
                throw LogUtils.logExceptionAndGet(logger,
                        data.getExecutable() + " depends on " + reference + " but none are found",
                        ComponentWithUnresolvedDependency::new);
            } else if(dependency.size() > 1 && !reference.isList()) {
                if(dependency.stream().filter(Predicate.not(ComponentData::isDefaultImpl)).count() != 1) {
                    throw LogUtils.logExceptionAndGet(logger,
                            data.getExecutable() + " depends on single " + reference + " but many were found",
                            ComponentWithMultipleOptionsForSingletonDependency::new);
                }
            }
        }
    }

    private void checkCyclicDependencies(List<ComponentData<?>> components) {
        for(ComponentData<?> component : components) {
            Stack<ComponentData<?>> stack = new Stack<>();
            try {
                checkCyclicDependencies(stack, component);
            } catch(CyclicDependencyException e) {
                throw LogUtils.logExceptionAndGet(logger,
                        "cyclic dependency in " + component, e,
                        CyclicDependencyException::new);
            }
        }
    }

    private void checkCyclicDependencies(Stack<ComponentData<?>> visited, ComponentData<?> current) {
        if(visited.contains(current)) {
            throw LogUtils.logExceptionAndGet(logger,
                    "cyclic dependency " + visited,
                    CyclicDependencyException::new);
        }
        if(current.getDependencies().isEmpty()) {
            return;
        }
        visited.push(current);
        current.getDependencies()
                .stream()
                .flatMap(List::stream)
                .forEach(dependency -> checkCyclicDependencies(visited, dependency));
        visited.pop();
    }

    private Map<ComponentData<?>,Integer> getOrder(List<ComponentData<?>> components) {
        List<ComponentData<?>> remaining = new ArrayList<>(components);
        Map<ComponentData<?>,Integer> map = new HashMap<>();
        int order = 0;
        while(!remaining.isEmpty()) {
            for(ComponentData<?> data : new ArrayList<>(remaining)) {
                if(data.getDependencies().stream().flatMap(List::stream).allMatch(map::containsKey)) {
                    map.put(data, order);
                    remaining.remove(data);
                }
            }
            order++;
        }
        return map;
    }
}
