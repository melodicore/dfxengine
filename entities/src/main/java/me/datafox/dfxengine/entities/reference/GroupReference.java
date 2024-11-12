package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.handles.api.Group;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupReference implements Reference {
    public String group;

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        Group group = map.getSpace().getGroups().get(this.group);
        if(group == null) {
            return Stream.empty();
        }
        return map.keySet()
                .stream()
                .filter(group.getHandles()::contains)
                .map(map::get);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("groupRef", GroupReference.class);
    }
}
