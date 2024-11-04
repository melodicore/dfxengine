package me.datafox.dfxengine.injector.serialization;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public final class ClassHierarchy {
    public HashMap<String, ClassData<?>> classes;

    public ArrayList<ExecutableData> methods;

    public ArrayList<ExecutableData> events;

    public ClassHierarchy(List<ClassData<?>> classes, List<ExecutableData> methods, List<ExecutableData> events) {
        this.classes = new HashMap<>(classes.stream().collect(Collectors.toMap(
                ClassData::getName, Function.identity())));
        this.methods = new ArrayList<>(methods);
        this.events = new ArrayList<>(events);
    }
}
