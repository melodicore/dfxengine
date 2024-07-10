package me.datafox.dfxengine.entities.definition.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.api.EntityLink;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.entities.link.ValueMapModifierLink;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapModifierLinkDefinition implements LinkDefinition {
    private String handle;
    private DataReference<ValueMap> output;
    private ModifierDefinition modifier;

    @Override
    public EntityLink build(Engine engine) {
        return new ValueMapModifierLink(this, engine);
    }
}
