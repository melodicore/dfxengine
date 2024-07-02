package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.Operation;

import java.util.List;

/**
 * @author datafox
 */
public class ValueMapOperationAction extends AbstractAction {
    private final Operation operation;
    private final List<Value> inputs;
    private final List<ValueMap> outputs;

    public ValueMapOperationAction(Handle handle, Operation operation, List<Value> inputs, List<ValueMap> outputs) {
        super(handle);
        this.operation = operation;
        this.inputs = inputs;
        this.outputs = outputs;
        if(inputs.size() != operation.getParameterCount()) {
            throw new IllegalArgumentException("input count does not match operation's parameter count");
        }
    }

    @Override
    public void run(Engine engine) {
        outputs.forEach(m -> m
                .apply(operation, inputs
                        .stream()
                        .map(Value::getValue)
                        .toArray(Numeral[]::new)));
    }
}
