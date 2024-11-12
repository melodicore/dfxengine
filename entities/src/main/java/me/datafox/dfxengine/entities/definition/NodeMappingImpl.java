package me.datafox.dfxengine.entities.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeMappingImpl implements NodeMapping {
    public int targetNode;

    public int targetOutput;

    @Component
    public static ClassTag getTag() {
        return new ClassTag("mapping", NodeMappingImpl.class);
    }
}
