package me.datafox.dfxengine.injector.serialization;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A serializable class that contains a full classpath scan.
 *
 * @author datafox
 */
@Data
@NoArgsConstructor
public final class ClassHierarchy implements Serializable {
    private static final long serialVersionUID = 1000L;

    /**
     * A map of {@link ClassData} objects with their fully qualified names as keys.
     */
    public HashMap<String, ClassData<?>> classes;

    /**
     * A list of {@link ExecutableData} objects that represent {@link Inject} and default constructors, as well as
     * {@link Component} and {@link Initialize} methods.
     */
    public ArrayList<ExecutableData<?>> methods;

    /**
     * A list of {@link ExecutableData} objects that represent {@link EventHandler} methods.
     */
    public ArrayList<ExecutableData<?>> events;

    /**
     * Public constructor for {@link ClassHierarchy}.
     *
     * @param classes {@link ClassData} objects for this hierarchy
     * @param methods {@link ExecutableData} objects for this hierarchy that represent {@link Inject} and default
     *                                      constructors, as well as {@link Component} and {@link Initialize} methods
     * @param events {@link ExecutableData} objects for this hierarchy that represent {@link EventHandler} methods
     */
    public ClassHierarchy(List<ClassData<?>> classes, List<ExecutableData<?>> methods, List<ExecutableData<?>> events) {
        this.classes = new HashMap<>(classes.stream().collect(Collectors.toMap(
                ClassData::getName, Function.identity())));
        this.methods = new ArrayList<>(methods);
        this.events = new ArrayList<>(events);
    }
}
