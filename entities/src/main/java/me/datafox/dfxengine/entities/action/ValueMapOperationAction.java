package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;

import java.util.List;

/**
 * @author datafox
 */
public class ValueMapOperationAction extends AbstractAction {
    private final Operation operation;
    private final List<Value> inputs;
    private final List<ValueMap> outputs;
    private final MathContext context;

    public ValueMapOperationAction(Handle handle, Operation operation, List<Value> inputs, List<ValueMap> outputs, MathContext context) {
        super(handle);
        this.operation = operation;
        this.inputs = inputs;
        this.outputs = outputs;
        if(context != null) {
            this.context = context;
        } else {
            this.context = MathContext.defaults();
        }
        if(inputs.size() != operation.getParameterCount()) {
            throw new IllegalArgumentException("input count does not match operation's parameter count");
        }
    }

    @Override
    public void run(Engine engine) {
        outputs.forEach(m -> m
                .apply(operation, context, inputs
                        .stream()
                        .map(Value::getValue)
                        .toArray(Numeral[]::new)));
    }
}
