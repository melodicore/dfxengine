package me.datafox.dfxengine.entities.component;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.component.Engine;
import me.datafox.dfxengine.entities.api.component.EntityFactory;
import me.datafox.dfxengine.entities.api.component.NodeResolver;
import me.datafox.dfxengine.entities.api.component.SerializationHandler;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.PackageDefinition;
import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
@Getter
public class EngineImpl implements Engine {
    private static final Numeral ZERO = Numerals.of(0);

    private final EntityFactory factory;

    private final EntityHandlesImpl handles;

    private final NodeResolver resolver;

    private final SerializationHandler<?, ?> serializer;

    private final List<PackageDefinition> packages;

    private final HandleMap<EntityDefinition> multiEntityDefinitions;

    private final HandleMap<List<Entity>> entities;

    private final HandleMap<List<Entity>> entitiesInternal;

    private final HandleMap<List<Entity>> entitiesInternalUnmodifiableLists;

    private final Map<Numeral, List<EntitySystem>> systems;

    private final Map<Numeral, Numeral> systemIntervals;

    private Numeral delta;

    @Inject
    public EngineImpl(EntityFactory factory, EntityHandlesImpl handles, NodeResolver resolver, SerializationHandler<?, ?> serializer) {
        this.factory = factory;
        this.handles = handles;
        this.resolver = resolver;
        this.serializer = serializer;
        packages = new ArrayList<>();
        multiEntityDefinitions = new HashHandleMap<>(handles.getEntitySpace());
        entitiesInternal = new HashHandleMap<>(handles.getEntitySpace());
        entitiesInternalUnmodifiableLists = new HashHandleMap<>(handles.getEntitySpace());
        entities = entitiesInternalUnmodifiableLists.unmodifiable();
        systems = new HashMap<>();
        systemIntervals = new HashMap<>();
    }

    @Override
    public List<Entity> getEntities(String handle) {
        return entities.get(handle);
    }

    @Override
    public List<Entity> getEntities(Handle handle) {
        return entities.get(handle);
    }

    @Override
    public Entity getEntity(String handle) {
        List<Entity> list = entities.get(handle);
        if(list.size() != 1) {
            throw new IllegalArgumentException("not a single entity");
        }
        return list.get(0);
    }

    @Override
    public Entity getEntity(Handle handle) {
        List<Entity> list = entities.get(handle);
        if(list.size() != 1) {
            throw new IllegalArgumentException("not a single entity");
        }
        return list.get(0);
    }

    @Override
    public Entity createMultiEntity(String handle) {
        return createMultiEntity(handles.getEntityHandle(handle));
    }

    @Override
    public Entity createMultiEntity(Handle handle) {
        return createMultiEntities(List.of(handle)).get(0);
    }

    @Override
    public List<Entity> createMultiEntities(Collection<Handle> handles) {
        if(!handles.stream().map(Handle::getTags)
                .allMatch(set -> set.contains(this.handles.getMultiEntityTag()))) {
            throw new IllegalArgumentException("not multi entities");
        }
        if(!multiEntityDefinitions.containsKeys(handles)) {
            throw new IllegalArgumentException("no definition for multi entities");
        }
        List<Entity> list = handles.stream()
                .map(multiEntityDefinitions::get)
                .map(factory::buildEntity)
                .peek(this::addEntity)
                .collect(Collectors.toList());
        init();
        return list;
    }

    @Override
    public boolean addPackage(PackageDefinition definition) {
        if(definition.getDependencies().stream()
                .allMatch(dependency -> packages.stream()
                        .map(pack -> pack.getId() + pack.getVersion())
                        .anyMatch(id -> id.startsWith(dependency)))) {
            packages.add(definition);
            return true;
        }
        return false;
    }

    @Override
    public void loadPackages(boolean keepState) {
        if(keepState) {
            //TODO: Save state
        }
        multiEntityDefinitions.clear();
        entitiesInternal.clear();
        entitiesInternalUnmodifiableLists.clear();
        systems.clear();
        systemIntervals.clear();
        packages.stream()
                .map(PackageDefinition::getSpaces)
                .flatMap(List::stream)
                .forEach(handles::createSpaces);
        packages.stream()
                .map(PackageDefinition::getEntities)
                .flatMap(List::stream)
                .filter(Predicate.not(this::isMultiEntityDefinition))
                .map(factory::buildEntity)
                .forEach(this::addEntity);
        multiEntityDefinitions.putAll(packages.stream()
                .map(PackageDefinition::getEntities)
                .flatMap(List::stream)
                .filter(Predicate.not(this::isMultiEntityDefinition))
                .collect(Collectors.toMap(
                        e -> handles.getEntityHandle(e.getHandle()),
                        Function.identity())));
        packages.stream()
                .map(PackageDefinition::getSystems)
                .flatMap(List::stream)
                .map(factory::buildSystem)
                .forEach(this::addSystem);
        init();
        if(keepState) {
            //TODO: Load state
        }
    }

    private void init() {
        resolver.run(entities.values()
                .stream()
                .flatMap(List::stream)
                .map(Entity::getComponents)
                .map(HandleMap::values)
                .flatMap(Collection::stream)
                .map(EntityComponent::getTrees)
                .flatMap(List::stream)
                .filter(tree -> tree.getAttributes().contains(NodeTreeAttribute.INIT)));
    }

    private boolean isMultiEntityDefinition(EntityDefinition definition) {
        return handles.getEntityHandle(definition.getHandle())
                .getTags()
                .contains(handles.getMultiEntityTag());
    }

    @Override
    public void update(float delta) {
        update(Numerals.of(delta));
    }

    @Override
    public void update(Numeral delta) {
        systems.forEach((n, s) -> update(n, s, delta));
    }

    private void addEntity(Entity entity) {
        getEntityList(entity.getHandle()).add(entity);
    }

    private void addSystem(EntitySystem entitySystem) {
    }

    private List<Entity> getEntityList(Handle handle) {
        if(entitiesInternal.containsKey(handle)) {
            return entitiesInternal.get(handle);
        }
        List<Entity> list = new ArrayList<>();
        entitiesInternal.put(handle, list);
        entitiesInternalUnmodifiableLists.put(handle, Collections.unmodifiableList(list));
        return list;
    }

    private void update(Numeral interval, List<EntitySystem> systems, Numeral delta) {
        if(Numerals.isZero(interval)) {
            this.delta = delta;
            update(systems);
            this.delta = ZERO;
            return;
        }
        Numeral counter = systemIntervals.get(interval);
        if(counter == null) {
            update(systems);
            systemIntervals.put(interval, ZERO);
            return;
        }
        counter = Operations.add(counter, delta);
        if(Numerals.compare(counter, interval) >= 0) {
            this.delta = counter;
            update(systems);
            this.delta = ZERO;
            counter = Operations.subtract(counter, interval);
        }
        systemIntervals.put(interval, counter);
    }

    private void update(List<EntitySystem> systems) {
        resolver.run(systems
                .stream()
                .map(EntitySystem::getTrees)
                .flatMap(List::stream));
    }
}
