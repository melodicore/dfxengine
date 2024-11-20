package me.datafox.dfxengine.values.modifier;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.invalidParameterCount;

/**
 * Wraps an {@link Operation} as a {@link Modifier}. Will always use {@link Value#getValue()} for the Operation
 * parameters.
 *
 * @author datafox
 */
public class OperationModifier extends AbstractModifier {
    private static final Value[] EMPTY_ARR = new Value[0];

    private final Operation operation;

    /**
     * Public constructor for {@link OperationModifier} that uses {@link SourceOperation}.
     *
     * @param priority priority for this modifier
     * @param operation {@link SourceOperation} to be used
     */
    public OperationModifier(int priority, SourceOperation operation) {
        this(priority, operation, EMPTY_ARR);
    }

    /**
     * Public constructor for {@link OperationModifier} that uses {@link SingleParameterOperation}.
     *
     * @param priority priority for this modifier
     * @param operation {@link SingleParameterOperation} to be used
     * @param parameter parameter {@link Value} for the {@link SingleParameterOperation}
     */
    public OperationModifier(int priority, SingleParameterOperation operation, Value parameter) {
        this(priority, operation, new Value[] {parameter});
    }

    /**
     * Public constructor for {@link OperationModifier} that uses {@link DualParameterOperation}.
     *
     * @param priority priority for this modifier
     * @param operation {@link DualParameterOperation} to be used
     * @param parameter1 first parameter {@link Value} for the {@link DualParameterOperation}
     * @param parameter2 second parameter {@link Value} for the {@link DualParameterOperation}
     */
    public OperationModifier(int priority, DualParameterOperation operation, Value parameter1, Value parameter2) {
        this(priority, operation, new Value[] {parameter1, parameter2});
    }

    /**
     * Public constructor for {@link OperationModifier} that uses {@link Operation}.
     *
     * @param priority priority for this modifier
     * @param operation {@link Operation} to be used
     * @param parameters parameter {@link Value Values} for the {@link Operation}
     *
     * @throws IllegalArgumentException if the number of parameters is not equal to
     * {@link Operation#getParameterCount()} for the {@link Operation}
     */
    public OperationModifier(int priority, Operation operation, Value ... parameters) {
        this(LoggerFactory.getLogger(OperationModifier.class), priority, operation, parameters);
    }

    /**
     * Protected constructor for {@link OperationModifier}.
     *
     * @param logger {@link Logger} for this modifier
     * @param priority priority for this modifier
     * @param operation {@link Operation} to be used
     * @param parameters parameter {@link Value Values} for the {@link Operation}
     *
     * @throws IllegalArgumentException if the number of parameters is not equal to
     * {@link Operation#getParameterCount()} for the {@link Operation}
     */
    protected OperationModifier(Logger logger, int priority, Operation operation, Value ... parameters) {
        super(logger, priority, parameters);

        if(parameters.length != operation.getParameterCount()) {
            LogUtils.logExceptionAndGet(logger,
                    invalidParameterCount(operation.getParameterCount(), parameters.length),
                    IllegalArgumentException::new);
        }

        this.operation = operation;
    }

    /**
     * Applies this modifier to a {@link Numeral} and returns the result.
     *
     * @param source {@link Numeral} to be modified
     * @return result of the modifier
     */
    @Override
    public Numeral apply(Numeral source) {
        return operation.apply(source,
                Arrays.stream(getParameters())
                        .map(Value::getValue)
                        .toArray(Numeral[]::new));
    }
}
