package me.datafox.dfxengine.text.text;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.text.api.Formatter;
import me.datafox.dfxengine.text.api.Text;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class StaticText implements Text {
    private final String text;

    public StaticText(String text) {
        this.text = text;
    }

    public <T> StaticText(T object, Formatter<T> formatter) {
        this(formatter.format(object));
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void invalidate() {}

    @Override
    public Collection<Dependency> getDependencies() {
        return Set.of();
    }

    @Override
    public void invalidateDependencies() {}

    @Override
    public boolean addDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean addDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    @Override
    public boolean removeDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean removeDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    @Override
    public boolean containsDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean containsDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return false;
    }

    @Override
    public boolean containsDependenciesRecursive(Collection<? extends Dependency> dependencies) {
        return false;
    }

    @Override
    public Stream<Dependency> dependencyStream() {
        return Stream.empty();
    }

    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return Stream.empty();
    }

    @Override
    public String toString() {
        return getText();
    }
}

