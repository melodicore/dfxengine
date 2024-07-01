package me.datafox.dfxengine.entities.definition.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityLink;
import me.datafox.dfxengine.entities.api.definition.LinkDefinition;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueModifierLink implements LinkDefinition {
    private DataReference<Value> output;
    private ModifierDefinition definition;

    @Override
    public EntityLink build(Engine engine) {
        Modifier modifier = definition.build(engine);
        output.get(engine).forEach(v -> v.addModifier(modifier));
        return new Link(modifier);
    }

    @Data
    public static class Link implements EntityLink {
        private final Modifier modifier;
    }
}
