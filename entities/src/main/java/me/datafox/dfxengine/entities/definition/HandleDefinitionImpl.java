package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class HandleDefinitionImpl implements HandleDefinition {
    public String handle;

    public ArrayList<String> groups;

    public ArrayList<String> tags;

    @Builder
    public HandleDefinitionImpl(String handle, @Singular List<String> groups, @Singular List<String> tags) {
        this.handle = handle;
        this.groups = new ArrayList<>(groups);
        this.tags = new ArrayList<>(tags);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("handle", HandleDefinitionImpl.class);
    }
}
