package me.datafox.dfxengine.values.utils;

import lombok.Data;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;
import me.datafox.dfxengine.math.operation.MappingOperationChain;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class Modifiers {
    public static final SpecialValue SOURCE_VALUE = new SpecialValue(MappingOperationChain.SOURCE_NUMERAL);
    public static final SpecialValue RESULT_VALUE = new SpecialValue(MappingOperationChain.RESULT_NUMERAL);

    public static OperationModifier add(int priority, Value addend) {
        return new OperationModifier(priority, Operations::add, addend);
    }

    public static OperationModifier subtract(int priority, Value subtrahend) {
        return new OperationModifier(priority, Operations::subtract, subtrahend);
    }

    public static OperationModifier subtractReversed(int priority, Value minuend) {
        return new OperationModifier(priority, reverse(Operations::subtract), minuend);
    }

    public static OperationModifier multiply(int priority, Value multiplier) {
        return new OperationModifier(priority, Operations::multiply, multiplier);
    }

    public static OperationModifier divide(int priority, Value divisor) {
        return new OperationModifier(priority, Operations::divide, divisor);
    }

    public static OperationModifier divideReversed(int priority, Value dividend) {
        return new OperationModifier(priority, reverse(Operations::divide), dividend);
    }

    public static OperationModifier power(int priority, Value exponent) {
        return new OperationModifier(priority, Operations::power, exponent);
    }

    public static OperationModifier powerReversed(int priority, Value base) {
        return new OperationModifier(priority, reverse(Operations::power), base);
    }

    public static OperationModifier log(int priority) {
        return new OperationModifier(priority, Operations::log);
    }

    public static OperationModifier log2(int priority) {
        return new OperationModifier(priority, Operations::log2);
    }

    public static OperationModifier log10(int priority) {
        return new OperationModifier(priority, Operations::log10);
    }

    public static OperationModifier logN(int priority, Value base) {
        return new OperationModifier(priority, Operations::logN, base);
    }

    public static OperationModifier logReversed(int priority, Value value) {
        return new OperationModifier(priority, reverse(Operations::logN), value);
    }

    public static OperationModifier percentMultiply(int priority, Value percentage, Value multiplier) {
        return new OperationModifier(priority, MappingOperationChain.builder()
                .operation(Operations::multiply)
                .operation(Operations::add)
                .operation(Operations::multiply)
                .build(),
                List.of(
                        //Multiply percentage with multiplier
                        percentage, multiplier,
                        //Add 1 to result
                        RESULT_VALUE, new StaticValue(1),
                        //Multiply source with result
                        SOURCE_VALUE, RESULT_VALUE));
    }

    public static OperationModifier powerMultiply(int priority, Value base, Value exponent) {
        return new OperationModifier(priority, MappingOperationChain.builder()
                .operation(Operations::power)
                .operation(Operations::multiply)
                .build(),
                List.of(
                        //Exponentiation
                        base, exponent,
                        //Multiply source with result
                        SOURCE_VALUE, RESULT_VALUE));
    }

    private static SingleParameterOperation reverse(SingleParameterOperation operation) {
        return (source, parameter) -> operation.apply(parameter, source);
    }

    @Data
    public static class SpecialValue implements Value {
        private final MappingOperationChain.SpecialNumeral numeral;

        @Override
        public Numeral getBase() {
            return numeral;
        }

        @Override
        public Numeral getValue() {
            return numeral;
        }

        @Override
        public boolean isStatic() {
            return true;
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
