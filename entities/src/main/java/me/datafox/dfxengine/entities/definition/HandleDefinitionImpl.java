package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
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

    @Override
    public void append(HandleDefinition other) {
        List<String> list = new LinkedList<>(other.getGroups());
        list.removeAll(groups);
        groups.addAll(list);
        list = new LinkedList<>(other.getTags());
        list.removeAll(tags);
        tags.addAll(list);
    }
}
