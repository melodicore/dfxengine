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

/**
 * @author datafox
 */
public interface Value extends Dependency, Dependent, Handled {
    Numeral getBase();

    Numeral getValue();

    boolean isStatic();

    boolean canConvert(NumeralType type);

    boolean convert(NumeralType type) throws ExtendedArithmeticException;

    boolean convertIfAllowed(NumeralType type);

    boolean toInteger();

    boolean toDecimal();

    boolean toSmallestType();

    void set(Numeral value);

    void apply(SourceOperation operation, MathContext context);

    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    void apply(Operation operation, MathContext context, Numeral ... parameters);

    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    Collection<Modifier> getModifiers();

    boolean addModifier(Modifier modifier);

    boolean addModifiers(Collection<Modifier> modifiers);

    boolean removeModifier(Modifier modifier);

    boolean removeModifiers(Collection<Modifier> modifiers);

    boolean containsModifier(Modifier modifier);

    boolean containsModifiers(Collection<Modifier> modifiers);

    default void apply(SourceOperation operation, MathContext.MathContextBuilder<?,?> builder) {
        apply(operation, builder.build());
    }

    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    default void apply(SingleParameterOperation operation, MathContext.MathContextBuilder<?,?> builder,
                       Numeral parameter) {
        apply(operation, builder.build(), parameter);
    }

    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    default void apply(DualParameterOperation operation, MathContext.MathContextBuilder<?,?> builder,
                       Numeral parameter1, Numeral parameter2) {
        apply(operation, builder.build(), parameter1, parameter2);
    }

    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    default void apply(Operation operation,
                       MathContext.MathContextBuilder<?,?> builder, Numeral ... parameters) {
        apply(operation, builder.build(), parameters);
    }

    default void apply(Operation operation, Numeral ... parameters) {
        apply(operation, MathContext.defaults(), parameters);
    }

    default boolean compare(Comparison comparison, ComparisonContext.ComparisonContextBuilder<?,?> builder,
                            Numeral other) {
        return compare(comparison, builder.build(), other);
    }

    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }
}
