package me.datafox.dfxengine.values.operation;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public final class MappingOperationChain implements Operation {
    public static final int SOURCE_ID = 0;
    public static final int RESULT_ID = 1;
    public static final SpecialNumeral SOURCE_NUMERAL = new SpecialNumeral(SOURCE_ID);
    public static final SpecialNumeral RESULT_NUMERAL = new SpecialNumeral(RESULT_ID);

    private final List<Operation> operations;
    @Getter
    private final int parameterCount;

    private MappingOperationChain(List<Operation> operations) {
        this.operations = operations;
        parameterCount = operations.stream().mapToInt(Operation::getParameterCount).sum() + operations.size();
    }

    @Override
    public Numeral apply(Numeral source, List<Numeral> parameters) throws IllegalArgumentException {
        if(parameters.size() != parameterCount) {
            throw new IllegalArgumentException("invalid parameter count");
        }
        parameters = parameters.stream()
                .map(numeral -> replaceSpecial(numeral, source, SOURCE_ID))
                .collect(Collectors.toList());
        int nextIndex = 0;
        Numeral result = source;
        for(Operation operation : operations) {
            Numeral lastResult = result;
            result = apply(operation, parameters.subList(nextIndex, operation.getParameterCount()).stream()
                    .map(numeral -> replaceSpecial(numeral, lastResult, RESULT_ID))
                    .collect(Collectors.toList()));
            nextIndex = operation.getParameterCount() + 1;
        }
        return result;
    }

    private Numeral replaceSpecial(Numeral source, Numeral target, int id) {
        if(source instanceof SpecialNumeral &&
                ((SpecialNumeral) source).getId() == id) {
            return target;
        }
        return source;
    }

    private Numeral apply(Operation operation, List<Numeral> parameters) {
        return operation.apply(parameters.get(0), parameters.subList(1, parameters.size() - 1));
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
            return new MappingOperationChain(operations);
        }
    }

    @Data
    public static class SpecialNumeral implements Numeral {
        private final int id;

        @Override
        public Number getNumber() {
            return null;
        }

        @Override
        public NumeralType getType() {
            return null;
        }

        @Override
        public Numeral convert(NumeralType type) throws ArithmeticException {
            return this;
        }

        @Override
        public Numeral convertIfAllowed(NumeralType type) {
            return this;
        }

        @Override
        public Numeral convertToDecimal() {
            return this;
        }

        @Override
        public boolean canConvert(NumeralType type) {
            return false;
        }

        @Override
        public Numeral toSmallestType() {
            return this;
        }

        @Override
        public int intValue() throws ArithmeticException {
            return 0;
        }

        @Override
        public long longValue() throws ArithmeticException {
            return 0;
        }

        @Override
        public BigInteger bigIntValue() {
            return BigInteger.ZERO;
        }

        @Override
        public float floatValue() throws ArithmeticException {
            return 0;
        }

        @Override
        public double doubleValue() throws ArithmeticException {
            return 0;
        }

        @Override
        public BigDecimal bigDecValue() {
            return BigDecimal.ZERO;
        }

        @Override
        public int compareTo(Numeral o) {
            if(o instanceof SpecialNumeral) {
                return Integer.compare(getId(), ((SpecialNumeral) o).getId());
            }
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(!(o instanceof Numeral)) {
                return false;
            }
            Numeral numeral = (Numeral) o;
            return numeral.getNumber() == null;
        }
    }
}
