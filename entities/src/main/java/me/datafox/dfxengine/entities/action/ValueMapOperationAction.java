package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.action.ValueMapOperationActionDefinition;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
public class ValueMapOperationAction extends AbstractAction {
    private final ValueMapOperationActionDefinition definition;
    private final Operation operation;
    private final List<Value> inputs;
    private final List<ValueMap> outputs;
    private final MathContext context;

    public ValueMapOperationAction(ValueMapOperationActionDefinition definition, Engine engine) {
        super(definition.getHandle(), engine);
        this.definition = definition;
        operation = definition.getOperation().build(engine);
        if(definition.getInputs().size() != operation.getParameterCount()) {
            throw new IllegalArgumentException("input count does not match operation's parameter count");
        }
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        if(definition.getContext() != null) {
            context = definition.getContext().build(engine);
        } else {
            context = MathContext.defaults();
        }
    }

    @Override
    public void run() {
        outputs.forEach(m -> m
                .apply(operation, context, inputs
                        .stream()
                        .map(Value::getValue)
                        .toArray(Numeral[]::new)));
    }

    @Override
    public void link() {
        EntityUtils.assertSingleAndStream(getEngine(), definition.getInputs()).forEach(inputs::add);
        definition.getOutputs().get(getEngine()).forEach(outputs::add);
    }

    @Override
    public void clear() {
        inputs.clear();
        outputs.clear();
    }
}
