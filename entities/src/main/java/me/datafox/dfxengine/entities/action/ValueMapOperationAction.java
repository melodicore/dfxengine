package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.action.ValueMapOperationActionDefinition;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

import java.util.List;

/**
 * @author datafox
 */
public class ValueMapOperationAction extends AbstractAction {
    public static final ActionParameters.Key<Operation> OPERATION = ActionParameters.key("operation", (SingleParameterOperation) Operations::add);
    public static final ActionParameters.Key<List<Value>> INPUTS = ActionParameters.key("inputs", List.of(new StaticValue(Numerals.of(1))));
    public static final ActionParameters.Key<List<ValueMap>> OUTPUTS = ActionParameters.key("outputs", List.of());
    public static final ActionParameters.Key<MathContext> CONTEXT = ActionParameters.key("context", MathContext.defaults());

    private final ValueMapOperationActionDefinition definition;

    public ValueMapOperationAction(ValueMapOperationActionDefinition definition, Engine engine) {
        super(definition.getHandle(), engine);
        this.definition = definition;
    }

    @Override
    public void run(ActionParameters parameters) {
        parameters.get(OUTPUTS)
                .stream()
                .filter(this::checkImmutable)
                .forEach(m -> m
                .apply(parameters.get(OPERATION),
                        parameters.get(CONTEXT),
                        parameters.get(INPUTS)
                                .stream()
                                .map(Value::getValue)
                                .toArray(Numeral[]::new)));
    }

    @Override
    public void link() {
    }

    @Override
    public void clear() {
    }

    private boolean checkImmutable(ValueMap map) {
        if(map.isImmutable()) {
            getEngine().getLogger()
                    .warn(String.format("%s is immutable and cannot be modified by ValueMapOperationAction", map));
            return false;
        }
        return true;
    }
}
