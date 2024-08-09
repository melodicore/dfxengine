package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class SpaceDefinitionImpl implements SpaceDefinition {
    public String id;
    public List<String> groups;
    public List<HandleDefinition> handles;

    @Builder
    public SpaceDefinitionImpl(String id, @Singular List<String> groups, @Singular List<HandleDefinition> handles) {
        this.id = id;
        this.groups = new ArrayList<>(groups);
        this.handles = new ArrayList<>(handles);
    }

    @Override
    public void append(SpaceDefinition other) {
        List<String> list = new LinkedList<>(other.getGroups());
        list.removeAll(groups);
        groups.addAll(list);
        Map<String,HandleDefinition> map = handles.stream()
                .collect(Collectors.toMap(
                        HandleDefinition::getId,
                        Function.identity()));
        other.getHandles().forEach(handle -> appendHandle(handle, map));
    }

    private void appendHandle(HandleDefinition handle, Map<String,HandleDefinition> map) {
        if(map.containsKey(handle.getId())) {
            map.get(handle.getId()).append(handle);
            return;
        }
        handles.add(handle);
    }
}
