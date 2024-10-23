package me.datafox.dfxengine.entities.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;

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
}
