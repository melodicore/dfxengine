package me.datafox.dfxengine.math.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.modifier.Modifier;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;

import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public interface Value extends Dependency, Dependent, Handled {
    Numeral getBase();

    Numeral getValue();

    void convert(NumeralType type) throws ArithmeticException;

    boolean convertIfAllowed(NumeralType type);

    boolean convertToDecimal();

    boolean canConvert(NumeralType type);

    void toSmallestType();

    void set(Numeral value, MathContext context);

    void apply(SourceOperation operation, MathContext context);

    void apply(SingleParameterOperation operation, Numeral parameter, MathContext context);

    void apply(Operation operation, List<Numeral> parameters, MathContext context);

    boolean compare(Comparison comparison, Numeral other, ComparisonContext context);

    Collection<Modifier> getModifiers();

    boolean addModifier(Modifier modifier);

    boolean addModifiers(Collection<Modifier> modifiers);

    boolean removeModifier(Modifier modifier);

    boolean removeModifiers(Collection<Modifier> modifiers);

    boolean containsModifier(Modifier modifier);

    boolean containsModifiers(Collection<Modifier> modifiers);

    default void set(Numeral value) {
        set(value, MathContext.getDefaults());
    }

    default void apply(SourceOperation operation) {
        apply(operation, MathContext.getDefaults());
    }

    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, parameter, MathContext.getDefaults());
    }

    default void apply(Operation operation, List<Numeral> parameters) {
        apply(operation, parameters, MathContext.getDefaults());
    }

    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, other, ComparisonContext.getDefaults());
    }
}
