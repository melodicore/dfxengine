package me.datafox.dfxengine.values.modifier;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public final class OperationModifier extends AbstractModifier {
    private final Operation operation;

    public OperationModifier(int priority, SourceOperation operation) {
        this(priority, operation, List.of());
    }

    public OperationModifier(int priority, SingleParameterOperation operation, Value parameter) {
        this(priority, operation, List.of(parameter));
    }

    public OperationModifier(int priority, Operation operation, List<Value> parameters) {
        super(LoggerFactory.getLogger(OperationModifier.class), priority, parameters);
        if(parameters.size() != operation.getParameterCount()) {
            LogUtils.logExceptionAndGet(logger, "invalid parameter count", IllegalArgumentException::new);
        }
        this.operation = operation;
    }

    @Override
    public Numeral apply(Numeral source) {
        return operation.apply(source,
                getParameters()
                        .stream()
                        .map(Value::getValue)
                        .collect(Collectors.toList()));
    }
}
