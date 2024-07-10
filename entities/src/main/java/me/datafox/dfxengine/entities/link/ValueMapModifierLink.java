package me.datafox.dfxengine.entities.link;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.link.ValueMapModifierLinkDefinition;
import me.datafox.dfxengine.values.api.Modifier;

/**
 * @author datafox
 */
@Getter
public class ValueMapModifierLink extends AbstractLink {
    private final ValueMapModifierLinkDefinition definition;
    private Modifier modifier;

    public ValueMapModifierLink(ValueMapModifierLinkDefinition definition, Engine engine) {
        super(definition.getHandle(), engine);
        this.definition = definition;
    }

    @Override
    public void link() {
        modifier = definition.getModifier().build(getEngine());
        definition.getOutput().get(getEngine()).forEach(v -> v.addModifier(modifier));
    }

    @Override
    public void clear() {
        definition.getOutput().get(getEngine()).forEach(v -> v.removeModifier(modifier));
        modifier = null;
    }
}
