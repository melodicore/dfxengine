package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.exception.CyclicDependencyException;
import me.datafox.dfxengine.injector.internal.*;
import me.datafox.dfxengine.injector.serialization.ClassHierarchy;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.*;

/**
 * Builder for the {@link InjectorImpl}. For general information, see the
 * <a href="https://engine.datafox.me/doku.php?id=injector">wiki</a>.
 *
 * @author datafox
 */
public class InjectorBuilder {
    private static ClassHierarchy hierarchy;

    /**
     * Scans the class hierarchy with default settings.
     */
    public static void scan() {
        hierarchy = ClassScanner.builder().build().scan();
    }

    /**
     * Loads an already scanned class hierarchy.
     *
     * @param hierarchy scanned class hierarchy
     */
    public static void load(ClassHierarchy hierarchy) {
        InjectorBuilder.hierarchy = hierarchy;
    }

    private final Logger logger;

    /**
     * Constructor for the builder
     */
    public InjectorBuilder() {
        logger = LoggerFactory.getLogger(InjectorBuilder.class);
    }

    /**
     * Builds the {@link InjectorImpl} according to the scan done at compile time.
     *
     * @return the {@link InjectorImpl}
     */
    public InjectorImpl build() {
        ComponentDataFactory factory = new ComponentDataFactory(hierarchy.getClasses());

        List<ComponentData<?>> components = hierarchy.getMethods()
                .stream()
                .map(factory::buildComponentData)
                .collect(Collectors.toList());

        logComponents(components);

        List<EventData<?>> eventData = hierarchy.getEvents()
                .stream()
                .map(factory::buildEventData)
                .collect(Collectors.toList());

        parseDependencies(components);

        checkCyclicDependencies(components);

        Map<ComponentData<?>,Integer> orderMap = getOrder(components);

        return new InjectorImpl(factory, components.stream()
                .sorted(Comparator.comparing(orderMap::get)), eventData);
    }

    private void logComponents(List<ComponentData<?>> components) {
        logger.info(componentsFound(components.size()));
        if(!components.isEmpty()) {
            logger.debug(components(components));
        }
    }

    private void parseDependencies(List<ComponentData<?>> components) {
        logger.info(BUILDING_DEPENDENCY_GRAPH);
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
                        .filter(data -> data.getReference() != null)
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
        }
    }

    private List<ComponentData<?>> parseDependency(ClassReference<?> classReference, List<ComponentData<?>> components) {
        return components
                .stream()
                .filter(data -> data.getReference() != null)
                .filter(data -> classReference.isAssignableFrom(data.getReference().getActualReference()))
                .collect(Collectors.toList());
    }

    private void checkCyclicDependencies(List<ComponentData<?>> components) {
        logger.info(CHECKING_CYCLIC);
        for(ComponentData<?> component : components) {
            checkCyclicDependencies(new ArrayDeque<>(), component);
        }
    }

    private void checkCyclicDependencies(Deque<ComponentData<?>> visited, ComponentData<?> current) {
        if(visited.contains(current)) {
            throw LogUtils.logExceptionAndGet(logger,
                    cyclicDependencyDetected(current, visited),
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
