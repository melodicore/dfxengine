package me.datafox.dfxengine.values.modifier;

import lombok.Getter;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.Modifier;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author datafox
 */
public abstract class AbstractModifier extends DependencyDependent implements Modifier {
    @Getter
    protected final int priority;
    private final List<Value> parameters;

    protected AbstractModifier(Logger logger, int priority, List<Value> parameters) {
        super(logger);
        this.priority = priority;
        this.parameters = parameters;
        parameters.stream()
                .filter(Predicate.not(Value::isStatic))
                .forEach(val -> val.addDependency(this));
    }

    @Override
    protected void onInvalidate() {}

    protected Value getParameter(int index) {
        return parameters.get(index);
    }

    protected List<Value> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
