package me.datafox.dfxengine.entities;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.state.EngineStateImpl;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import org.slf4j.Logger;

import java.util.*;
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
    private final HandleManager handleManager;
    private final HandleMap<Entity> entities;
    private final List<EntityLink> links;
    private final Map<String,DataPack> dataPacks;
    private final SortedSet<EntitySystem> systems;

    @Inject
    public EngineImpl(Logger logger, Injector injector, HandleManager handleManager, EntityHandles ignoredHandles, List<EntitySystem> systems) {
        this.logger = logger;
        this.injector = injector;
        this.handleManager = handleManager;
        entities = new TreeHandleMap<>(EntityHandles.getEntities(), logger);
        links = new ArrayList<>();
        dataPacks = new HashMap<>();
        this.systems = new TreeSet<>(systems);
    }

    @Override
    public HandleMap<Entity> getEntities() {
        return entities.unmodifiable();
    }

    @Override
    public List<EntityLink> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public Map<String,DataPack> getDataPacks() {
        return Collections.unmodifiableMap(dataPacks);
    }

    @Override
    public void registerPack(DataPack pack) {
        if(!pack.getDependencies().stream().allMatch(dataPacks::containsKey)) {
            throw new IllegalArgumentException("unresolved pack dependency");
        }
        dataPacks.put(pack.getId(), pack);
    }

    @Override
    public void deregisterPack(DataPack pack, boolean removeDependents, boolean keepState) {
        if(!dataPacks.containsKey(pack.getId())) {
            logger.warn("pack not found");
            return;
        }
        Set<String> removed = new HashSet<>();
        removed.add(pack.getId());
        if(dataPacks.values()
                .stream()
                .map(DataPack::getDependencies)
                .anyMatch(s -> s.contains(pack.getId()))) {
            if(removeDependents) {
                removed.addAll(getDependents(pack.getId()).collect(Collectors.toSet()));
            } else {
                logger.warn("dependents found but removeDependents is false");
                return;
            }
        }
        removed.forEach(dataPacks::remove);
        loadPacks(keepState);
    }

    @Override
    public void loadPacks(boolean keepState) {
        EngineState state = null;
        if(keepState) {
            state = getState();
        }
        entities.clear();
        links.clear();
        systems.clear();
        dataPacks.values()
                .stream()
                .map(DataPack::getSpaces)
                .flatMap(List::stream)
                .forEach(this::registerSpace);
        dataPacks.values()
                .stream()
                .map(DataPack::getEntities)
                .flatMap(List::stream)
                .map(this::buildEntity)
                .forEach(entities::putHandled);
        links.addAll(dataPacks
                .values()
                .stream()
                .map(DataPack::getLinks)
                .flatMap(List::stream)
                .map(l -> l.build(this))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        systems.addAll(dataPacks
                .values()
                .stream()
                .map(DataPack::getSystems)
                .flatMap(List::stream)
                .map(s -> s.build(this))
                .collect(Collectors.toList()));
        if(state != null) {
            setState(state);
        }
    }

    @Override
    public void setState(EngineState state) {
        state.getEntities().forEach(this::setEntityState);
    }

    @Override
    public EngineState getState() {
        return EngineStateImpl
                .builder()
                .entities(entities
                        .values()
                        .stream()
                        .map(Entity::getState)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void update(float delta) {
        systems.forEach(s -> s.update(this, delta));
    }

    private Stream<String> getDependents(String id) {
        return dataPacks.values()
                .stream()
                .filter(d -> d.getDependencies().contains(id))
                .flatMap(d -> Stream.concat(
                        Stream.of(d.getId()),
                        getDependents(d.getId())))
                .distinct();
    }

    private Entity buildEntity(EntityDefinition definition) {
        return EntityImpl
                .builder()
                .logger(logger)
                .handle(EntityHandles
                        .getEntities()
                        .getOrCreateHandle(definition.getHandle()))
                .components(definition
                        .getComponents()
                        .stream()
                        .map(this::buildComponent)
                        .collect(Collectors.toList()))
                .build();
    }

    private EntityComponent buildComponent(ComponentDefinition definition) {
        return EntityComponentImpl
                .builder()
                .logger(logger)
                .handle(EntityHandles
                        .getComponents()
                        .getOrCreateHandle(definition.getHandle()))
                .data(definition
                        .getData()
                        .stream()
                        .map(d -> d.build(this))
                        .collect(Collectors.toList()))
                .build();
    }

    private void registerSpace(SpaceDefinition definition) {
        Space space = handleManager.getOrCreateSpace(definition.getId());
        definition.getGroups().forEach(space::getOrCreateGroup);
        definition.getHandles().forEach(h -> registerHandle(space, h));
    }

    private void registerHandle(Space space, HandleDefinition definition) {
        Handle handle = space.getOrCreateHandle(definition.getId());
        definition.getGroups()
                .stream()
                .map(space::getOrCreateGroup)
                .forEach(g -> g.getHandles().add(handle));
        definition.getTags().forEach(handle.getTags()::add);

    }

    private void setEntityState(EntityState state) {
        if(!entities.containsKey(state.getHandle())) {
            logger.warn(String.format("State contains unknown entity %s", state.getHandle()));
            return;
        }
        entities.get(state.getHandle()).setState(state);
    }
}
