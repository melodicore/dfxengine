package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.DataPack;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class DataPackImpl implements DataPack {
    private String id;
    private List<String> dependencies;
    private String developer;
    private List<SpaceDefinition> spaces;
    private List<EntityDefinition> entities;
    private List<SystemDefinition> systems;

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
}
