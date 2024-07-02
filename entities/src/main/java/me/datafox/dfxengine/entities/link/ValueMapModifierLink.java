package me.datafox.dfxengine.entities.link;

import lombok.Data;
import me.datafox.dfxengine.entities.api.link.EntityLink;
import me.datafox.dfxengine.entities.api.link.ModifierLink;
import me.datafox.dfxengine.values.api.Modifier;

/**
 * @author datafox
 */
@Data
public class ValueMapModifierLink implements ModifierLink {
    private final Modifier modifier;
}
