package me.datafox.dfxengine.values.modifier;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author datafox
 */
public class OperationModifier extends AbstractModifier {
    private static final Value[] EMPTY_ARR = new Value[0];

    private final Operation operation;

    public OperationModifier(int priority, SourceOperation operation) {
        this(priority, operation, EMPTY_ARR);
    }

    public OperationModifier(int priority, SingleParameterOperation operation, Value parameter) {
        this(priority, operation, new Value[] {parameter});
    }

    public OperationModifier(int priority, DualParameterOperation operation, Value parameter1, Value parameter2) {
        this(priority, operation, new Value[] {parameter1, parameter2});
    }

    public OperationModifier(int priority, Operation operation, Value ... parameters) {
        this(LoggerFactory.getLogger(OperationModifier.class), priority, operation, parameters);
    }

    protected OperationModifier(Logger logger, int priority, Operation operation, Value ... parameters) {
        super(logger, priority, parameters);

        if(parameters.length != operation.getParameterCount()) {
            LogUtils.logExceptionAndGet(logger,
                    "invalid parameter count", IllegalArgumentException::new);
        }

        this.operation = operation;
    }

    @Override
    public Numeral apply(Numeral source) {
        return operation.apply(source,
                Arrays.stream(getParameters())
                        .map(Value::getValue)
                        .toArray(Numeral[]::new));
    }
}
