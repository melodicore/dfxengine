package me.datafox.dfxengine.entities.reference.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * @author datafox
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SelectorValueMapDataReference extends SelectorDataReference<ValueMap> {
    private Reference<EntityComponent> component;
    private Selector selector;

    @Override
    protected String getTypeHandle() {
        return EntityHandles.getValueMapType().getId();
    }
}
