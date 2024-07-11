package me.datafox.dfxengine.entities.definition.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityLink;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.link.ValueModifierLink;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueModifierLinkDefinition implements LinkDefinition {
    private String handle;
    private Reference<Value> output;
    private ModifierDefinition modifier;

    @Override
    public EntityLink build(Engine engine) {
        return new ValueModifierLink(this, engine);
    }
}
