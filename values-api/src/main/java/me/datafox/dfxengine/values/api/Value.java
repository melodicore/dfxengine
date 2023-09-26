package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public interface Value extends Dependency, Dependent, Handled {
    Numeral getBase();

    Numeral getValue();

    boolean isStatic();

    boolean convert(NumeralType type) throws ExtendedArithmeticException;

    boolean convertIfAllowed(NumeralType type);

    boolean convertToDecimal();

    boolean canConvert(NumeralType type);

    void toSmallestType();

    void set(Numeral value, MathContext context);

    void apply(SourceOperation operation, MathContext context);

    void apply(SingleParameterOperation operation, Numeral parameter, MathContext context);

    void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2, MathContext context);

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
        set(value, MathContext.defaults());
    }

    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, parameter, MathContext.defaults());
    }

    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, parameter1, parameter2, MathContext.defaults());
    }

    default void apply(Operation operation, List<Numeral> parameters) {
        apply(operation, parameters, MathContext.defaults());
    }

    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, other, ComparisonContext.defaults());
    }
}
