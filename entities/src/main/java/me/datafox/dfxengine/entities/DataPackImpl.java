package me.datafox.dfxengine.entities;

import lombok.*;
import me.datafox.dfxengine.entities.api.DataPack;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;

import java.util.List;
import java.util.Set;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataPackImpl implements DataPack {
    private String id;
    @Singular
    private Set<String> dependencies;
    private String developer;
    @Singular
    private List<SpaceDefinition> spaces;
    @Singular
    private List<EntityDefinition> entities;
    @Singular
    private List<LinkDefinition> links;
    @Singular
    private List<SystemDefinition> systems;
}
