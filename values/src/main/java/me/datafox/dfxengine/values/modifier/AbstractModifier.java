package me.datafox.dfxengine.values.modifier;

import lombok.Getter;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author datafox
 */
public abstract class AbstractModifier extends DependencyDependent implements Modifier {
    @Getter
    protected final int priority;
    private final Value[] parameters;

    protected AbstractModifier(Logger logger, int priority, Value ... parameters) {
        super(logger);
        this.priority = priority;
        this.parameters = parameters;

        Arrays.stream(parameters)
                .filter(Predicate.not(Value::isStatic))
                .forEach(val -> val.addDependency(this));
    }

    @Override
    protected void onInvalidate() {}

    protected Value getParameter(int index) {
        return parameters[index];
    }

    protected Value[] getParameters() {
        return parameters;
    }
}
