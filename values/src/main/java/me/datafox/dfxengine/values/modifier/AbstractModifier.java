package me.datafox.dfxengine.values.modifier;

import lombok.Getter;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * An abstract implementation of {@link Modifier}. Adds itself as a {@link Dependency} to parameter {@link Value Values}
 * on instantiation.
 *
 * @author datafox
 */
public abstract class AbstractModifier extends DependencyDependent implements Modifier {
    @Getter
    protected final int priority;
    private final Value[] parameters;

    /**
     * @param logger {@link Logger} for this modifier
     * @param priority priority for this modifier
     * @param parameters parameter {@link Value Values} for this modifier
     */
    protected AbstractModifier(Logger logger, int priority, Value ... parameters) {
        super(logger);
        this.priority = priority;
        this.parameters = parameters;

        Arrays.stream(parameters)
                .filter(Predicate.not(Value::isStatic))
                .forEach(val -> val.addDependency(this));
    }

    /**
     * This method should be treated as the equivalent of {@link Dependency#invalidate()} for classes extending
     * DependencyDependent. This exists to avoid the need for calling {@code super.invalidate()} or
     * {@link #invalidateDependencies()} which may cause hidden or hard to find bugs when forgotten.
     */
    @Override
    protected void onInvalidate() {}

    /**
     * @param index parameter index
     * @return parameter {@link Value} for the specified index
     *
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    protected Value getParameter(int index) {
        return parameters[index];
    }

    /**
     * @return array of parameter {@link Value Values}
     */
    protected Value[] getParameters() {
        return parameters;
    }
}
