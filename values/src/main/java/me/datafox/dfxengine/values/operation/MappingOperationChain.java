package me.datafox.dfxengine.values.operation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public final class MappingOperationChain implements Operation {
    public static SpecialNumeral resultNumeral(int operationIndex) {
        return new SpecialNumeral(operationIndex);
    }

    public static SpecialNumeral sourceNumeral() {
        return new SpecialNumeral(-1);
    }

    private final Logger logger;
    private final Operation[] operations;
    @Getter
    private final int parameterCount;

    public MappingOperationChain(Operation ... operations) {
        logger = LoggerFactory.getLogger(MappingOperationChain.class);
        this.operations = operations;
        parameterCount = Arrays.stream(operations)
                .mapToInt(Operation::getParameterCount)
                .sum() + operations.length;
    }

    @Override
    public Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException {
        if(parameters.length != parameterCount) {
            throw LogUtils.logExceptionAndGet(logger,
                    "invalid parameter count", IllegalArgumentException::new);
        }
        System.out.println(source + Arrays.toString(parameters));
        checkSpecial(parameters);
        Numeral[] results = new Numeral[operations.length + 1];
        results[0] = source;
        int nextIndex = 0;
        for(int i=0;i<operations.length;i++) {
            Operation operation = operations[i];
            results[i+1] = apply(operation, Arrays.stream(
                    parameters, nextIndex, nextIndex + operation.getParameterCount() + 1)
                    .map(numeral -> replaceSpecial(numeral, results))
                    .toArray(Numeral[]::new));
            nextIndex += operation.getParameterCount() + 1;
        }
        return results[operations.length];
    }

    private Numeral replaceSpecial(Numeral source, Numeral[] results) {
        if(source instanceof SpecialNumeral) {
            return results[((SpecialNumeral) source).getId() + 1];
        }

        return source;
    }

    private void checkSpecial(Numeral[] parameters) {
        int nextIndex = 0;
        for(int i=0;i<operations.length;i++) {
            Operation operation = operations[i];
            int ii = i;
            if(Arrays.stream(parameters, nextIndex, nextIndex + operation.getParameterCount())
                    .filter(SpecialNumeral.class::isInstance)
                    .map(SpecialNumeral.class::cast)
                    .mapToInt(SpecialNumeral::getId)
                    .anyMatch(value -> value >= ii)) {
                throw LogUtils.logExceptionAndGet(logger,
                        "Reference to future operation", IllegalArgumentException::new);
            }
            nextIndex = operation.getParameterCount() + 1;
        }
    }

    private Numeral apply(Operation operation, Numeral[] parameters) {
        if(parameters.length == 1) {
            return operation.apply(parameters[0]);
        }
        return operation.apply(parameters[0],
                Arrays.copyOfRange(parameters, 1, parameters.length));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Operation> operations;

        Builder() {
            operations = new ArrayList<>();
        }

        public Builder operation(SourceOperation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operation(SingleParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operation(DualParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operation(Operation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operations(Collection<? extends Operation> operations) {
            if(operations == null) {
                return this;
            }
            this.operations.addAll(operations);
            return this;
        }

        public Builder clearOperations() {
            this.operations.clear();
            return this;
        }

        public MappingOperationChain build() {
            return new MappingOperationChain(operations.toArray(Operation[]::new));
        }
    }

    @EqualsAndHashCode
    public static class SpecialNumeral implements Numeral {
        private final int id;

        public SpecialNumeral(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        /**
         * @implNote always returns {@link Integer} 0
         */
        @Override
        public Number getNumber() {
            return 0;
        }

        /**
         * @implNote always returns {@link NumeralType#INT}
         */
        @Override
        public NumeralType getType() {
            return NumeralType.INT;
        }

        /**
         * @implNote unsupported operation, always returns false
         */
        @Override
        public boolean canConvert(NumeralType type) {
            return false;
        }

        /**
         * @implNote unsupported operation, always returns itself
         */
        @Override
        public Numeral convert(NumeralType type) {
            return this;
        }

        /**
         * @implNote unsupported operation, always returns itself
         */
        @Override
        public Numeral convertIfAllowed(NumeralType type) {
            return this;
        }

        /**
         * @implNote unsupported operation, always returns itself
         */
        @Override
        public Numeral toInteger() {
            return this;
        }

        /**
         * @implNote unsupported operation, always returns itself
         */
        @Override
        public Numeral toDecimal() {
            return this;
        }

        /**
         * @implNote unsupported operation, always returns itself
         */
        @Override
        public Numeral toSmallestType() {
            return this;
        }

        /**
         * @implNote always returns 0
         */
        @Override
        public int intValue() {
            return 0;
        }

        /**
         * @implNote always returns 0L
         */
        @Override
        public long longValue() {
            return 0L;
        }

        /**
         * @implNote always returns {@link BigInteger#ZERO}
         */
        @Override
        public BigInteger bigIntValue() {
            return BigInteger.ZERO;
        }

        /**
         * @implNote always returns 0f
         */
        @Override
        public float floatValue() {
            return 0;
        }

        /**
         * @implNote always returns 0d
         */
        @Override
        public double doubleValue() {
            return 0;
        }

        /**
         * @implNote always returns {@link BigDecimal#ZERO}
         */
        @Override
        public BigDecimal bigDecValue() {
            return BigDecimal.ZERO;
        }

        /**
         * @implNote if o is also a SpecialNumeral, the return values of {@link #getId()} are compared. Otherwise, 1 is
         * returned
         */
        @Override
        public int compareTo(Numeral o) {
            if(o instanceof SpecialNumeral) {
                return Integer.compare(getId(), ((SpecialNumeral) o).getId());
            }
            return 1;
        }

        @Override
        public String toString() {
            if(id == -1) {
                return "Source Value";
            }

            return String.format("Operation %s Result", id);
        }
    }
}
