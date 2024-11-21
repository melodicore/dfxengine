package me.datafox.dfxengine.entities.component;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.component.Engine;
import me.datafox.dfxengine.entities.api.component.EntityFactory;
import me.datafox.dfxengine.entities.api.component.NodeResolver;
import me.datafox.dfxengine.entities.api.component.SerializationHandler;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.PackageDefinition;
import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;
import me.datafox.dfxengine.entities.api.state.*;
import me.datafox.dfxengine.entities.entity.EntityComponentImpl;
import me.datafox.dfxengine.entities.exception.InvalidStateException;
import me.datafox.dfxengine.entities.exception.NoStateConverterException;
import me.datafox.dfxengine.entities.state.ComponentStateImpl;
import me.datafox.dfxengine.entities.state.EngineStateImpl;
import me.datafox.dfxengine.entities.state.EntityStateImpl;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
@Getter
public class EngineImpl implements Engine {
    private static final Numeral ZERO = Numerals.of(0);

    private final Logger logger;

    private final EntityFactory factory;

    private final EntityHandlesImpl handles;

    private final NodeResolver resolver;

    private final SerializationHandler<?, ?> serializer;

    private final Map<DataType<?>, StateConverter<?, ?>> stateConverters;

    private final List<PackageDefinition> packages;

    private final HandleMap<EntityDefinition> multiEntityDefinitions;

    private final HandleMap<List<Entity>> entities;

    private final HandleMap<List<Entity>> entitiesInternal;

    private final HandleMap<List<Entity>> entitiesInternalUnmodifiableLists;

    private final Map<Numeral, List<EntitySystem>> systems;

    private final Map<Numeral, Numeral> systemIntervals;

    private Numeral delta;

    @Inject
    public EngineImpl(Logger logger, EntityFactory factory, EntityHandlesImpl handles, NodeResolver resolver, SerializationHandler<?, ?> serializer, List<StateConverter<?, ?>> stateConverters) {
        this.logger = logger;
        this.factory = factory;
        this.handles = handles;
        this.resolver = resolver;
        this.serializer = serializer;
        this.stateConverters = stateConverters.stream()
                .collect(Collectors.toMap(
                        StateConverter::getType,
                        Function.identity()));
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
        List<Entity> list = createMultiEntitiesInternal(handles);
        setState(getState());
        return list;
    }

    private List<Entity> createMultiEntitiesInternal(Collection<Handle> handles) {
        if(!handles.stream().map(Handle::getTags)
                .allMatch(set -> set.contains(this.handles.getMultiEntityTag()))) {
            throw new IllegalArgumentException("not multi entities");
        }
        if(!multiEntityDefinitions.containsKeys(handles)) {
            throw new IllegalArgumentException("no definition for multi entities");
        }
        return handles.stream()
                .map(multiEntityDefinitions::get)
                .map(factory::buildEntity)
                .peek(this::addEntity)
                .collect(Collectors.toList());
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
        EngineState state = null;
        if(keepState) {
            state = getState();
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
        if(state != null) {
            setState(state);
        } else {
            init();
        }
    }

    @Override
    public EngineState getState() {
        return EngineStateImpl
                .builder()
                .entities(entitiesInternal
                        .values()
                        .stream()
                        .flatMap(this::getEntityStates)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void setState(EngineState state) {
        clearMultiEntities();
        state.getMultiEntityCounts().forEach(this::createMultiEntitiesFromState);
        init();
        state.getEntities().forEach(this::setEntityState);
    }

    private void clearMultiEntities() {
        multiEntityDefinitions.keySet().stream().map(entitiesInternal::get).forEach(List::clear);
    }

    private void createMultiEntitiesFromState(String handle, int count) {
        Handle h = handles.getEntityHandle(handle);
        createMultiEntitiesInternal(IntStream
                .range(0, count)
                .mapToObj(i -> h)
                .collect(Collectors.toList()));
    }

    private void setEntityState(EntityState state) {
        Handle handle = handles.getEntityHandle(state.getHandle());
        if(!entitiesInternal.containsKey(handle)) {
            logger.warn("No entity found for state " + state.getHandle());
            return;
        }
        List<Entity> list = entitiesInternal.get(handle);
        if(list.isEmpty()) {
            logger.warn("No entity found for state " + state.getHandle());
            return;
        }
        if(state.getIndex() >= list.size()) {
            throw LogUtils.logExceptionAndGet(logger,
                    "State references a multi entity outside specified range",
                    InvalidStateException::new);
        }
        setEntityState(list.get(state.getIndex()), state);
    }

    private void setEntityState(Entity entity, EntityState state) {
        state.getComponents().forEach(component -> setComponentState(entity, component));
    }

    private void setComponentState(Entity entity, ComponentState state) {
        EntityComponent component = entity.getComponents().get(state.getHandle());
        if(component == null) {
            logger.warn("State references a component " + state.getHandle() + " that does not exist in entity " + state.getHandle());
            return;
        }
        setComponentState(component, state);
    }

    private void setComponentState(EntityComponent component, ComponentState state) {
        state.getData().forEach(data -> setDataState(component, data));
    }

    private <T> void setDataState(EntityComponent component, DataState<T> state) {
        HandleMap<EntityData<T>> map = component.getData(state.getType());
        if(map == null || !map.containsKey(state.getHandle())) {
            logger.warn("State references data " + state.getType() + ": " + state.getHandle() + " that does not exist in component " + state.getHandle() + " in entity " + component.getEntity().getHandle().getId());
            return;
        }
        state.setState(map.get(state.getHandle()).getData());
    }

    @Override
    public String saveState() {
        return serializer.serialize(getState());
    }

    @Override
    public void loadState(String state) {
        setState(serializer.deserialize(EngineStateImpl.class, state));
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

    private Stream<EntityState> getEntityStates(List<Entity> entities) {
        List<EntityState> states = new ArrayList<>();
        for(int i = 0; i < entities.size(); i++) {
            getEntityState(entities.get(i), i).ifPresent(states::add);
        }
        return states.stream();
    }

    private Optional<EntityState> getEntityState(Entity entity, int index) {
        List<ComponentState> states = entity
                .getComponents()
                .values()
                .stream()
                .map(this::getComponentState)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        if(states.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(EntityStateImpl
                .builder()
                .handle(entity.getHandle().getId())
                .index(index)
                .components(states)
                .build());
    }

    private Optional<ComponentState> getComponentState(EntityComponent component) {
        List<DataState<?>> states = ((EntityComponentImpl) component).getData()
                .values()
                .stream()
                .map(HandleMap::values)
                .flatMap(Collection::stream)
                .filter(EntityData::isStateful)
                .map(this::getDataState)
                .collect(Collectors.toList());

        if(states.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ComponentStateImpl
                .builder()
                .handle(component.getHandle().getId())
                .data(states)
                .build());
    }

    @SuppressWarnings("unchecked")
    private <T> DataState<T> getDataState(EntityData<T> data) {
        if(!stateConverters.containsKey(data.getType())) {
            throw LogUtils.logExceptionAndGet(logger,
                    "No state converter for " + data.getType(),
                    NoStateConverterException::new);
        }
        return ((StateConverter<T, ?>) stateConverters.get(data.getType())).createState(data);
    }
}
