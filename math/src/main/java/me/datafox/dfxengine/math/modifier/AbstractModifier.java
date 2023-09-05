package me.datafox.dfxengine.math.modifier;

import lombok.Getter;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.math.api.modifier.Modifier;
import me.datafox.dfxengine.math.api.Value;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

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
        parameters.forEach(val -> val.addDependency(this));
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
