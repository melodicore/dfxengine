package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class HandleDefinitionImpl implements HandleDefinition {
    public String id;
    public List<String> groups;
    public List<String> tags;

    @Builder
    public HandleDefinitionImpl(String id, @Singular List<String> groups, @Singular List<String> tags) {
        this.id = id;
        this.groups = new ArrayList<>(groups);
        this.tags = new ArrayList<>(tags);
    }
}
