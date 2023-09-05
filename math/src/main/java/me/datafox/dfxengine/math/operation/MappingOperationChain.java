package me.datafox.dfxengine.math.operation;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.Value;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.modifier.Modifier;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public final class MappingOperationChain implements Operation {
    private static final int SOURCE_ID = 0;
    private static final int RESULT_ID = 1;
    public static final SpecialNumeral SOURCE_NUMERAL = new SpecialNumeral(SOURCE_ID);
    public static final SpecialNumeral RESULT_NUMERAL = new SpecialNumeral(RESULT_ID);
    public static final SpecialValue SOURCE_VALUE = new SpecialValue(SOURCE_NUMERAL);
    public static final SpecialValue RESULT_VALUE = new SpecialValue(RESULT_NUMERAL);

    private final List<Operation> operations;
    @Getter
    private final int parameterCount;

    private MappingOperationChain(List<Operation> operations) {
        this.operations = operations;
        parameterCount = operations.stream().mapToInt(Operation::getParameterCount).sum() + operations.size();
    }

    @Override
    public Numeral apply(Numeral source, List<Numeral> parameters) throws IllegalArgumentException {
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
    private static class SpecialNumeral implements Numeral {
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

    @Data
    private static class SpecialValue implements Value {
        private final SpecialNumeral numeral;

        @Override
        public Numeral getBase() {
            return numeral;
        }

        @Override
        public Numeral getValue() {
            return numeral;
        }

        @Override
        public void convert(NumeralType type) {}

        @Override
        public boolean convertIfAllowed(NumeralType type) {
            return false;
        }

        @Override
        public boolean convertToDecimal() {
            return false;
        }

        @Override
        public boolean canConvert(NumeralType type) {
            return false;
        }

        @Override
        public void toSmallestType() {}

        @Override
        public void set(Numeral value, MathContext context) {}

        @Override
        public void apply(SourceOperation operation, MathContext context) {}

        @Override
        public void apply(SingleParameterOperation operation, Numeral parameter, MathContext context) {}

        @Override
        public void apply(Operation operation, List<Numeral> parameters, MathContext context) {}

        @Override
        public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
            return false;
        }

        @Override
        public Collection<Modifier> getModifiers() {
            return Set.of();
        }

        @Override
        public boolean addModifier(Modifier modifier) {
            return false;
        }

        @Override
        public boolean addModifiers(Collection<Modifier> modifiers) {
            return false;
        }

        @Override
        public boolean removeModifier(Modifier modifier) {
            return false;
        }

        @Override
        public boolean removeModifiers(Collection<Modifier> modifiers) {
            return false;
        }

        @Override
        public boolean containsModifier(Modifier modifier) {
            return false;
        }

        @Override
        public boolean containsModifiers(Collection<Modifier> modifiers) {
            return false;
        }

        @Override
        public void invalidate() {}

        @Override
        public Collection<Dependency> getDependencies() {
            return Set.of();
        }

        @Override
        public void invalidateDependencies() {}

        @Override
        public boolean addDependency(Dependency dependency) {
            return false;
        }

        @Override
        public boolean addDependencies(Collection<Dependency> dependencies) {
            return false;
        }

        @Override
        public boolean removeDependency(Dependency dependency) {
            return false;
        }

        @Override
        public boolean removeDependencies(Collection<Dependency> dependencies) {
            return false;
        }

        @Override
        public boolean containsDependency(Dependency dependency) {
            return false;
        }

        @Override
        public boolean containsDependencies(Collection<Dependency> dependencies) {
            return false;
        }

        @Override
        public boolean containsDependencyRecursive(Dependency dependency) {
            return false;
        }

        @Override
        public boolean containsDependenciesRecursive(Collection<Dependency> dependencies) {
            return false;
        }

        @Override
        public Stream<Dependency> dependencyStream() {
            return Stream.empty();
        }

        @Override
        public Stream<Dependency> recursiveDependencyStream() {
            return Stream.empty();
        }

        @Override
        public Handle getHandle() {
            return null;
        }
    }
}
