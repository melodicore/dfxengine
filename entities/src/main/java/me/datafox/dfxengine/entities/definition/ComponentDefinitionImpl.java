package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.EntityComponentImpl;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ComponentDefinitionImpl implements ComponentDefinition {
    public String handle;
    public List<DataDefinition> data;
    public List<LinkDefinition> links;
    public List<ActionDefinition> actions;

    @Builder
    public ComponentDefinitionImpl(String handle,
                                   @Singular("data") List<DataDefinition> data,
                                   @Singular List<LinkDefinition> links,
                                   @Singular List<ActionDefinition> actions) {
        this.handle = handle;
        this.data = new ArrayList<>(data);
        this.links = new ArrayList<>(links);
        this.actions = new ArrayList<>(actions);
    }

    @Override
    public EntityComponent build(Engine engine) {
        return new EntityComponentImpl(this, engine);
    }
}
