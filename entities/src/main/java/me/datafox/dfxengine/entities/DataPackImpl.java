package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.DataPack;
import me.datafox.dfxengine.entities.api.definition.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class DataPackImpl implements DataPack {
    public String id;
    public List<String> dependencies;
    public String developer;
    public List<SpaceDefinition> spaces;
    public List<EntityDefinition> entities;
    public List<SystemDefinition> systems;

    @Builder
    public DataPackImpl(String id,
                        @Singular List<String> dependencies,
                        String developer,
                        @Singular List<SpaceDefinition> spaces,
                        @Singular List<EntityDefinition> entities,
                        @Singular List<SystemDefinition> systems) {
        this.id = id;
        this.dependencies = new ArrayList<>(dependencies);
        this.developer = developer;
        this.spaces = new ArrayList<>(spaces);
        this.entities = new ArrayList<>(entities);
        this.systems = new ArrayList<>(systems);
    }

    @Override
    public void append(DataPack other) {
        Map<String,SpaceDefinition> spaceMap = spaces.stream()
                .collect(Collectors.toMap(
                        SpaceDefinition::getId,
                        Function.identity()));
        Map<String,EntityDefinition> entityMap = entities.stream()
                .collect(Collectors.toMap(
                        EntityDefinition::getHandle,
                        Function.identity()));
        other.getSpaces().forEach(space -> appendSpace(space, spaceMap));
        other.getEntities().forEach(entity -> appendEntity(entity, entityMap));
        systems.addAll(other.getSystems());
    }

    private void appendSpace(SpaceDefinition space, Map<String,SpaceDefinition> map) {
        if(map.containsKey(space.getId())) {
            map.get(space.getId()).append(space);
            return;
        }
        spaces.add(space);
    }

    private void appendEntity(EntityDefinition entity, Map<String,EntityDefinition> map) {
        if(map.containsKey(entity.getHandle())) {
            map.get(entity.getHandle()).append(entity);
            return;
        }
        entities.add(entity);
    }
}
