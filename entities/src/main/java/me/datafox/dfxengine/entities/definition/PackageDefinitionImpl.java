package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.*;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class PackageDefinitionImpl implements PackageDefinition {
    public String id;

    public String version;

    public String developer;

    public ArrayList<String> dependencies;

    public ArrayList<SpaceDefinition> spaces;

    public ArrayList<EntityDefinition> entities;

    public ArrayList<SystemDefinition> systems;

    @Builder
    public PackageDefinitionImpl(String id,
                                 String version,
                                 String developer,
                                 @Singular List<String> dependencies,
                                 @Singular List<SpaceDefinition> spaces,
                                 @Singular List<EntityDefinition> entities,
                                 @Singular List<SystemDefinition> systems) {
        this.id = id;
        this.version = version;
        this.developer = developer;
        this.dependencies = new ArrayList<>(dependencies);
        this.spaces = new ArrayList<>(spaces);
        this.entities = new ArrayList<>(entities);
        this.systems = new ArrayList<>(systems);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("package", PackageDefinitionImpl.class);
    }

    @Component
    public static List<DefaultElement> getDefaultElements() {
        return List.of(new DefaultElement(PackageDefinitionImpl.class, "spaces", SpaceDefinitionImpl.class),
                new DefaultElement(PackageDefinitionImpl.class, "entities", EntityDefinitionImpl.class),
                new DefaultElement(PackageDefinitionImpl.class, "systems", SystemDefinitionImpl.class));
    }
}

