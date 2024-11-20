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

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.invalidParameterCount;

/**
 * An extension of the {@link OperationChain} which offers more flexible ways for routing parameters. There are two main
 * changes. Firstly, inputs are now counted as regular parameters. This means that {@link #getParameterCount()} now
 * returns the number of parameters for all specified {@link Operation Operations}, plus the amount of operations
 * themselves. The second change is the inclusion of the {@link SpecialNumeral}, which is used to request the source
 * {@link Numeral} and the result of any prior operation as an arbitrary parameter. Two static methods are provided for
 * these, {@link #sourceNumeral()} and {@link #resultNumeral(int)}, but you can also instantiate a SpecialNumeral
 * manually with {@link SpecialNumeral#SpecialNumeral(int)}, where -1 refers to the source and any positive value refers
 * to the operation with that index. Referring to an operation that has yet to be executed results in an
 * {@link IllegalArgumentException}.
 *
 * @author datafox
 */
public final class MappingOperationChain implements Operation {
    /**
     * Returns a {@link SpecialNumeral} that refers to the source {@link Numeral}.
     *
     * @return {@link SpecialNumeral} that refers to the source {@link Numeral}
     */
    public static SpecialNumeral sourceNumeral() {
        return new SpecialNumeral(-1);
    }

    /**
     * Returns a {@link SpecialNumeral} that refers to the result of the specified {@link Operation}.
     *
     * @param operationIndex index of the {@link Operation} to refer to
     * @return {@link SpecialNumeral} that refers to the result of the specified {@link Operation}
     */
    public static SpecialNumeral resultNumeral(int operationIndex) {
        return new SpecialNumeral(operationIndex);
    }

    private final Logger logger;
    private final Operation[] operations;
    /**
     * Returns the amount of parameters this operation expects.
     */
    @Getter
    private final int parameterCount;

    /**
     * Public constructor for {@link MappingOperationChain}.
     *
     * @param operations {@link Operation Operations} to associate with this operation
     */
    public MappingOperationChain(List<? extends Operation> operations) {
        logger = LoggerFactory.getLogger(MappingOperationChain.class);
        this.operations = operations.toArray(Operation[]::new);
        parameterCount = operations
                .stream()
                .mapToInt(Operation::getParameterCount)
                .sum() + operations.size();
    }

    /**
     * Applies this operation to the specified source {@link Numeral} with the specified parameters.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()}
     */
    @Override
    public Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters.length != parameterCount) {
            throw LogUtils.logExceptionAndGet(logger,
                    invalidParameterCount(parameterCount, parameters.length),
                    IllegalArgumentException::new);
        }
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

    /**
     * Creates a builder for a {@link MappingOperationChain}.
     *
     * @return builder for a {@link MappingOperationChain}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A Builder for {@link MappingOperationChain}.
     */
    public static class Builder {
        private final List<Operation> operations;

        private Builder() {
            operations = new ArrayList<>();
        }

        /**
         * A {@link SourceOperation} for the mapping operation chain.
         *
         * @param operation {@link SourceOperation} to add to the mapping operation chain
         * @return this builder
         */
        public Builder operation(SourceOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * A {@link SingleParameterOperation} for the mapping operation chain.
         *
         * @param operation {@link SingleParameterOperation} to add to the mapping operation chain
         * @return this builder
         */
        public Builder operation(SingleParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * A {@link DualParameterOperation} for the mapping operation chain.
         *
         * @param operation {@link DualParameterOperation} to add to the mapping operation chain
         * @return this builder
         */
        public Builder operation(DualParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * An {@link Operation} for the mapping operation chain.
         *
         * @param operation {@link Operation} to add to the mapping operation chain
         * @return this builder
         */
        public Builder operation(Operation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * {@link Operation Operations} for the mapping operation chain.
         *
         * @param operations {@link Operation Operations} to add to the mapping operation chain
         * @return this builder
         */
        public Builder operations(Collection<? extends Operation> operations) {
            if(operations == null) {
                return this;
            }
            this.operations.addAll(operations);
            return this;
        }

        /**
         * Clears all {@link Operation Operations} from the mapping operation chain.
         *
         * @return this builder
         */
        public Builder clearOperations() {
            this.operations.clear();
            return this;
        }

        /**
         * Builds the mapping operation chain.
         *
         * @return mapping operation chain initialized by this builder
         */
        public MappingOperationChain build() {
            return new MappingOperationChain(operations);
        }
    }

    /**
     * A special {@link MappingOperationChain}-specific extension of {@link Numeral} that is used to refer to the source
     * numeral and the result numerals of {@link Operation Operations}.
     */
    @EqualsAndHashCode
    public static class SpecialNumeral implements Numeral {
        private final int id;

        /**
         * Public constructor for {@link SpecialNumeral}.
         *
         * @param id index of the {@link Operation} whose result is being referred to, or -1 if referring to the source
         * {@link Numeral} instead
         */
        public SpecialNumeral(int id) {
            this.id = id;
        }

        /**
         * Returns the index of the {@link Operation} whose result is being referred to, or -1 if referring to the
         * source {@link Numeral} instead.
         *
         * @return index of the {@link Operation} whose result is being referred to, or -1 if referring to the source
         * {@link Numeral} instead
         */
        public int getId() {
            return id;
        }

        /**
         * This implementation always returns the {@link Integer} {@code 0}.
         *
         * @return {@link Number} backing this numeral
         */
        @Override
        public Number getNumber() {
            return 0;
        }

        /**
         * This implementation always returns {@link NumeralType#INT}.
         *
         * @return backing {@link Number}'s type
         */
        @Override
        public NumeralType getType() {
            return NumeralType.INT;
        }

        /**
         * This implementation always returns {@code false}.
         *
         * @param type ignored parameter
         * @return {@code false}
         */
        @Override
        public boolean canConvert(NumeralType type) {
            return false;
        }

        /**
         * This implementation always returns itself.
         *
         * @param type ignored parameter
         * @return this numeral
         */
        @Override
        public Numeral convert(NumeralType type) {
            return this;
        }

        /**
         * This implementation always returns itself.
         *
         * @param type ignored parameter
         * @return this numeral
         */
        @Override
        public Numeral convertIfAllowed(NumeralType type) {
            return this;
        }

        /**
         * This implementation always returns itself.
         *
         * @return this numeral
         */
        @Override
        public Numeral toInteger() {
            return this;
        }

        /**
         * This implementation always returns itself.
         *
         * @return this numeral
         */
        @Override
        public Numeral toDecimal() {
            return this;
        }

        /**
         * This implementation always returns itself.
         *
         * @return this numeral
         */
        @Override
        public Numeral toSmallestType() {
            return this;
        }

        /**
         * This implementation always returns {@code 0}.
         *
         * @return the value of this numeral as an {@code int}
         */
        @Override
        public int intValue() {
            return 0;
        }

        /**
         * This implementation always returns {@code 0L}.
         *
         * @return the value of this numeral as a {@code long}
         */
        @Override
        public long longValue() {
            return 0L;
        }

        /**
         * This implementation always returns {@link BigInteger#ZERO}.
         *
         * @return the value of this numeral as a {@link BigInteger}
         */
        @Override
        public BigInteger bigIntValue() {
            return BigInteger.ZERO;
        }

        /**
         * This implementation always returns {@code 0f}.
         *
         * @return the value of this numeral as a {@code float}
         */
        @Override
        public float floatValue() {
            return 0;
        }

        /**
         * This implementation always returns {@code 0d}.
         *
         * @return the value of this numeral as a {@code double}
         */
        @Override
        public double doubleValue() {
            return 0;
        }

        /**
         * This implementation always returns {@link BigDecimal#ZERO}.
         *
         * @return the value of this numeral as a {@link BigDecimal}
         */
        @Override
        public BigDecimal bigDecValue() {
            return BigDecimal.ZERO;
        }


        /**
         * Compares this numeral with the specified numeral for order. If the other numeral is also a SpecialNumeral,
         * they are compared with {@link Integer#compare(int, int)} using {@link #getId()}. Otherwise, 1 is returned.
         *
         * @param other numeral to be compared
         * @return a negative integer, zero, or a positive integer as this numeral is less than, equal to, or greater
         * than the specified numeral
         */
        @Override
        public int compareTo(Numeral other) {
            if(other instanceof SpecialNumeral) {
                return Integer.compare(getId(), ((SpecialNumeral) other).getId());
            }
            return 1;
        }

        /**
         * Returns the {@link String} representation of this numeral. Specifically, returns {@code Source Value} for id
         * {@code -1} and {@code Operation [id] Result} otherwise.
         *
         * @return {@link String} representation of this numeral
         */
        @Override
        public String toString() {
            if(id == -1) {
                return "Source Value";
            }

            return String.format("Operation %s Result", id);
        }
    }
}
