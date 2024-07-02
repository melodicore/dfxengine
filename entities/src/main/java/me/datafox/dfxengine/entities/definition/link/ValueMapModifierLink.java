package me.datafox.dfxengine.entities.definition.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.link.EntityLink;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapModifierLink implements LinkDefinition {
    private DataReference<ValueMap> output;
    private ModifierDefinition definition;

    @Override
    public EntityLink build(Engine engine) {
        Modifier modifier = definition.build(engine);
        output.get(engine).forEach(v -> v.addModifier(modifier));
        return new me.datafox.dfxengine.entities.link.ValueMapModifierLink(modifier);
    }

}
