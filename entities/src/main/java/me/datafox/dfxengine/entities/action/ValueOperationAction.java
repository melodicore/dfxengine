package me.datafox.dfxengine.entities.action;

import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.action.ValueOperationActionDefinition;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

import java.util.List;

/**
 * @author datafox
 */
public class ValueOperationAction extends AbstractAction {
    public static final ActionParameters.Key<Operation> OPERATION = ActionParameters.key((SingleParameterOperation) Operations::add);
    public static final ActionParameters.Key<List<Value>> INPUTS = ActionParameters.key(List.of(new StaticValue(Numerals.of(1))));
    public static final ActionParameters.Key<List<Value>> OUTPUTS = ActionParameters.key(List.of());
    public static final ActionParameters.Key<MathContext> CONTEXT = ActionParameters.key(MathContext.defaults());

    private final ValueOperationActionDefinition definition;

    public ValueOperationAction(ValueOperationActionDefinition definition, Engine engine) {
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

    private boolean checkImmutable(Value value) {
        if(value.isImmutable()) {
            getEngine().getLogger()
                    .warn(String.format("%s is immutable and cannot be modified by ValueOperationAction", value));
            return false;
        }
        return true;
    }
}
