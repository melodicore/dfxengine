package me.datafox.dfxengine.entities;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.definition.*;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.state.EngineStateImpl;
import me.datafox.dfxengine.entities.data.EntityDataTypes;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Component
public class EngineImpl implements Engine {
    @Getter
    private final Logger logger;
    @Getter
    private final Injector injector;
    @Getter
    private final SerializationHandler serializationHandler;
    private final HandleMap<List<Entity>> entities;
    private final HandleMap<EntityDefinition> multiDefinitions;
    private final Set<EntityListener> listeners;
    private final Map<String,DataPack> dataPacks;
    private final SortedSet<EntitySystem> systems;
    private final Queue<ActionInstance> actions;
    @Getter
    private Entity currentEntity;
    @Getter
    private EntityComponent currentComponent;
    boolean linked;

    @Inject
    public EngineImpl(Logger logger,
                      Injector injector,
                      SerializationHandler serializationHandler,
                      EntityHandles ignored,
                      EntityDataTypes ignored1) {
        this.logger = logger;
        this.injector = injector;
        this.serializationHandler = serializationHandler;
        entities = new HashHandleMap<>(EntityHandles.getEntities());
        multiDefinitions = new HashHandleMap<>(EntityHandles.getEntities());
        listeners = new LinkedHashSet<>();
        dataPacks = new LinkedHashMap<>();
        systems = new TreeSet<>();
        actions = new ArrayDeque<>();
        currentEntity = null;
        currentComponent = null;
        linked = false;
    }

    @Override
    public HandleMap<List<Entity>> getEntities() {
        return entities.unmodifiable();
    }

    @Override
    public void addEntityListener(EntityListener listener) {
        listeners.add(listener);
        logger.info(String.format("Added entity listener %s", listener));
    }

    @Override
    public void removeEntityListener(EntityListener listener) {
        listeners.remove(listener);
        logger.info(String.format("Removed entity listener %s", listener));
    }

    @Override
    public Entity createMultiEntity(Handle handle) {
        logger.info(String.format("Creating multi entity instance with handle %s", handle));
        if(!multiDefinitions.containsKey(handle)) {
            throw new IllegalArgumentException("not a multi entity");
        }
        clear();
        Entity entity = createMultiEntityInternal(handle);
        link();
        listeners.forEach(l -> l.added(entity));
        return entity;
    }

    @Override
    public List<Entity> createMultiEntities(List<Handle> handles) {
        logger.info(String.format("Creating multi entity instances with handles %s", handles));
        if(!multiDefinitions.containsKeys(handles)) {
            throw new IllegalArgumentException("not a multi entity");
        }
        clear();
        List<Entity> added = handles.stream().map(this::createMultiEntityInternal).collect(Collectors.toList());
        link();
        listeners.forEach(l -> added.forEach(l::added));
        return added;
    }

    @Override
    public void removeMultiEntity(Entity entity) {
        logger.info(String.format("Removing multi entity instance %s", entity));
        if(entity.isSingleton()) {
            throw new IllegalArgumentException("not a multi entity");
        }
        if(!entities.containsKey(entity.getHandle())) {
            logger.warn("entity not found");
            return;
        }
        clear();
        Entity removed = removeMultiEntityInternal(entity);
        link();
        listeners.forEach(l -> l.removed(removed));
    }

    @Override
    public void removeMultiEntities(Collection<Entity> entities) {
        logger.info(String.format("Removing multi entity instances %s", entities));
        if(entities.stream().anyMatch(Entity::isSingleton)) {
            throw new IllegalArgumentException("not a multi entity");
        }
        if(!this.entities.containsKeys(entities.stream().map(Entity::getHandle).collect(Collectors.toSet()))) {
            logger.warn("at least one entity not found");
        }
        clear();
        List<Entity> removed = entities.stream().map(this::removeMultiEntityInternal).collect(Collectors.toList());
        link();
        listeners.forEach(l -> removed.forEach(l::removed));
    }

    @Override
    public Map<String,DataPack> getDataPacks() {
        return Collections.unmodifiableMap(dataPacks);
    }

    @Override
    public void addPack(DataPack pack) {
        logger.info(String.format("Registering data pack %s", pack.getId()));
        if(dataPacks.containsKey(pack.getId())) {
            throw new IllegalArgumentException("pack with id already exists");
        }
        if(!pack.getDependencies().stream().allMatch(dataPacks::containsKey)) {
            throw new IllegalArgumentException("unsatisfied dependencies");
        }
        dataPacks.put(pack.getId(), pack);
    }

    @Override
    public void addSerializedPack(String pack) {
        addPack(serializationHandler.deserialize(DataPackImpl.class, pack));
    }

    @Override
    public void removePack(String id, boolean removeDependents) {
        logger.info(String.format("Unregistering data pack %s", id));
        if(!dataPacks.containsKey(id)) {
            throw new IllegalArgumentException("pack with id does not exist");
        }
        removePackInternal(id, removeDependents);
    }

    private void removePackInternal(String id, boolean removeDependents) {
        Set<String> dependents = dataPacks.values().stream().filter(d -> d.getDependencies().contains(id)).map(DataPack::getId).collect(Collectors.toSet());
        if(dependents.isEmpty()) {
            dataPacks.remove(id);
            return;
        }
        if(removeDependents) {
            dataPacks.remove(id);
            dependents.forEach(d -> removePackInternal(d, true));
            return;
        }
        throw new IllegalArgumentException("dependents found but removeDependents is false");
    }

    @Override
    public void loadPacks(boolean keepState) {
        if(dataPacks.isEmpty()) {
            throw new IllegalArgumentException("No data packs are registered");
        }
        EngineState state = null;
        if(keepState) {
            state = getState();
        }
        clear();
        EntityHandles.clear();
        entities.clear();
        multiDefinitions.clear();
        systems.clear();
        DataPack pack;
        if(dataPacks.size() == 1) {
            pack = dataPacks.values().iterator().next();
        } else {
            pack = new DataPackImpl();
            dataPacks.values().forEach(pack::append);
        }
        pack.getSpaces().forEach(EntityHandles::setSpace);
        pack.getEntities().forEach(this::createEntityInternal);
        pack.getSystems().stream()
                .map(s -> s.build(this))
                .forEach(systems::add);
        if(keepState && state != null) {
            setState(state);
        } else {
            link();
        }
    }

    @Override
    public void setState(EngineState state) {
        clear();
        state.getSingleEntities().forEach(this::loadSingleEntityState);
        if(state.getMultiEntities().isEmpty()) {
            clearMultiEntities();
            state.getMultiEntities().forEach(this::loadMultiEntityState);
        }
        link();
    }

    @Override
    public void setSerializedState(String state) {
        setState(serializationHandler.deserialize(EngineStateImpl.class, state));
    }

    @Override
    public EngineState getState() {
        EngineStateImpl.EngineStateImplBuilder builder = EngineStateImpl.builder();
        entities.forEach((h, l) -> getEntityState(l, builder));
        return builder.build();
    }

    @Override
    public String getSerializedState() {
        return serializationHandler.serialize(getState());
    }

    @Override
    public void scheduleAction(EntityAction action, ActionParameters parameters) {
        actions.offer(new ActionInstance(action, parameters));
        logger.info(String.format("Action %s scheduled with parameters %s", action, parameters));
    }

    @Override
    public void update(float delta) {
        ActionInstance action = actions.poll();
        while(action != null) {
            logger.info(String.format("Running action %s with parameters %s", action.getAction(), action.getParameters()));
            action.getAction().run(action.getParameters());
            action = actions.poll();
        }
        systems.forEach(s -> s.update(delta));
    }

    private void link() {
        logger.info("Linking entities and systems");
        if(linked) {
            return;
        }
        entities.values()
                .stream()
                .flatMap(List::stream)
                .forEach(this::linkEntity);
        systems.forEach(EntitySystem::link);
        linked = true;
    }

    private void clear() {
        logger.info("Clearing entity and system links");
        if(!linked) {
            return;
        }
        entities.values()
                .stream()
                .flatMap(List::stream)
                .forEach(this::clearEntity);
        systems.forEach(EntitySystem::clear);
        linked = false;
    }

    private void clearMultiEntities() {
        multiDefinitions.keySet().forEach(entities::remove);
    }

    private Entity createMultiEntityInternal(Handle handle) {
        Entity entity = multiDefinitions.get(handle).build(this);
        if(entities.containsKey(handle)) {
            entities.put(handle, Stream.concat(entities.get(handle).stream(),
                    Stream.of(entity)).collect(Collectors.toList()));
        } else {
            entities.put(handle, List.of(entity));
        }
        return entity;
    }

    private Entity removeMultiEntityInternal(Entity entity) {
        entities.put(entity.getHandle(), entities
                .get(entity.getHandle())
                .stream()
                .filter(Predicate.not(Predicate.isEqual(entity)))
                .collect(Collectors.toList()));
        return entity;
    }

    private void createEntityInternal(EntityDefinition definition) {
        if(!definition.isSingleton()) {
            multiDefinitions.put(EntityHandles.getEntities().getOrCreateHandle(definition.getHandle()), definition);
            return;
        }
        if(entities.containsKey(definition.getHandle())) {
            throw new IllegalArgumentException("entity with handle already exists");
        }
        Entity entity = definition.build(this);
        entities.put(entity.getHandle(), List.of(entity));
    }

    private void loadSingleEntityState(EntityState state) {
        List<Entity> list = entities.get(state.getHandle());
        if(list == null || list.isEmpty()) {
            logger.warn("entity not found");
            return;
        }
        Entity entity = list.get(0);
        if(list.size() != 1 || !entity.isSingleton()) {
            throw new IllegalArgumentException("trying to load singleton entity data to multi entity");
        }
        entity.setState(state);
    }

    private void loadMultiEntityState(String id, List<EntityState> states) {
        Handle handle = EntityHandles.getEntities().getOrCreateHandle(id);
        if(handle.getTags().contains(EntityHandles.getSingleTag())) {
            throw new IllegalArgumentException("trying to load multi entity data to singleton entity");
        }
        states.forEach(s -> createMultiEntityInternal(handle).setState(s));
    }

    private void getEntityState(List<Entity> entities, EngineStateImpl.EngineStateImplBuilder builder) {
        if(entities.isEmpty()) {
            return;
        }
        Entity entity = entities.get(0);
        if(entity.isSingleton()) {
            builder.singleEntity(entity.getState());
        } else {
            builder.multiEntity(entity.getHandle().getId(),
                    entities
                            .stream()
                            .map(Entity::getState)
                            .collect(Collectors.toList()));
        }
    }

    private void linkEntity(Entity entity) {
        currentEntity = entity;
        entity.getComponents().values().forEach(this::linkComponent);
        currentEntity = null;
    }

    private void linkComponent(EntityComponent component) {
        currentComponent = component;
        component.link();
        currentComponent = null;
    }

    private void clearEntity(Entity entity) {
        currentEntity = entity;
        entity.getComponents().values().forEach(this::clearComponent);
        currentEntity = null;
    }

    private void clearComponent(EntityComponent component) {
        currentComponent = component;
        component.clear();
        currentComponent = null;
    }

    @Data
    private static class ActionInstance {
        private final EntityAction action;
        private final ActionParameters parameters;
    }
}
