package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.comparison.MapComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author datafox
 */
public interface ValueMap extends HandleMap<Value> {
    void convert(NumeralType type) throws ExtendedArithmeticException;

    void convert(Collection<Handle> handles, NumeralType type) throws ExtendedArithmeticException;

    void convert(Map<Handle, NumeralType> types) throws ExtendedArithmeticException;

    void convertAllowed(NumeralType type);

    void toInteger();

    void toDecimal();

    void toSmallestType();

    void toSmallestType(Collection<Handle> handles);

    void set(Numeral value);

    void set(MapMathContext context, Collection<Handle> handles, Numeral value);

    void set(MapMathContext context, Map<Handle, Numeral> values);

    void apply(SourceOperation operation, MathContext context);

    void apply(SourceOperation operation, MapMathContext context, Collection<Handle> handles);

    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    void apply(SingleParameterOperation operation, MapMathContext context,
               Collection<Handle> handles, Numeral parameter);

    void apply(SingleParameterOperation operation, MapMathContext context, Map<Handle, Numeral> parameters);

    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    void apply(DualParameterOperation operation, MapMathContext context,
               Collection<Handle> handles, Numeral parameter1, Numeral parameter2);

    void apply(Operation operation, MathContext context, Numeral ... parameters);

    void apply(Operation operation, MapMathContext context, Collection<Handle> handles, Numeral ... parameters);

    void apply(Operation operation, MapMathContext context, Map<Handle, Numeral[]> parameters);

    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    boolean compare(Comparison comparison, MapComparisonContext context, Collection<Handle> handles, Numeral other);

    boolean compare(Comparison comparison, MapComparisonContext context, Map<Handle, Numeral> others);

    Map<Handle, Numeral> getBaseNumeralMap();

    Map<Handle, Numeral> getValueNumeralMap();

    Collection<Modifier> getModifiers();

    boolean addModifier(Modifier modifier);

    boolean addModifiers(Collection<Modifier> modifiers);

    boolean removeModifier(Modifier modifier);

    boolean removeModifiers(Collection<Modifier> modifiers);

    boolean containsModifier(Modifier modifier);

    boolean containsModifiers(Collection<Modifier> modifiers);

    default void set(MapMathContext.MapMathContextBuilder<?,?> builder, Collection<Handle> handles, Numeral value) {
        set(builder.build(), handles, value);
    }

    default void set(Collection<Handle> handles, Numeral value) {
        set(MapMathContext.defaults(), handles, value);
    }

    default void set(MapMathContext.MapMathContextBuilder<?,?> builder, Map<Handle, Numeral> values) {
        set(builder.build(), values);
    }

    default void set(Map<Handle, Numeral> values) {
        set(MapMathContext.defaults(), values);
    }

    default void apply(SourceOperation operation,
                       MathContext.MathContextBuilder<?,?> builder) {
        apply(operation, builder.build());
    }

    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    default void apply(SourceOperation operation,
                       MapMathContext.MapMathContextBuilder<?,?> builder, Collection<Handle> handles) {
        apply(operation, builder.build(), handles);
    }

    default void apply(SourceOperation operation, Collection<Handle> handles) {
        apply(operation, MapMathContext.defaults(), handles);
    }

    default void apply(SingleParameterOperation operation,
                       MapMathContext.MapMathContextBuilder<?,?> builder, Numeral parameter) {
        apply(operation, builder.build(), parameter);
    }

    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    default void apply(SingleParameterOperation operation, Collection<Handle> handles,
                       MapMathContext.MapMathContextBuilder<?,?> builder, Numeral parameter) {
        apply(operation, builder.build(), handles, parameter);
    }

    default void apply(SingleParameterOperation operation, Collection<Handle> handles, Numeral parameter) {
        apply(operation, MapMathContext.defaults(), handles, parameter);
    }

    default void apply(SingleParameterOperation operation,
                       MapMathContext.MapMathContextBuilder<?,?> builder, Map<Handle, Numeral> parameters) {
        apply(operation, builder.build(), parameters);
    }

    default void apply(SingleParameterOperation operation, Map<Handle, Numeral> parameters) {
        apply(operation, MapMathContext.defaults(), parameters);
    }

    default void apply(DualParameterOperation operation,
                       MathContext.MathContextBuilder<?,?> builder, Numeral parameter1, Numeral parameter2) {
        apply(operation, builder.build(), parameter1, parameter2);
    }

    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    default void apply(DualParameterOperation operation, MapMathContext.MapMathContextBuilder<?,?> builder,
                       Collection<Handle> handles, Numeral parameter1, Numeral parameter2) {
        apply(operation, builder.build(), handles, parameter1, parameter2);
    }

    default void apply(DualParameterOperation operation, Collection<Handle> handles,
                       Numeral parameter1, Numeral parameter2) {
        apply(operation, MapMathContext.defaults(), handles, parameter1, parameter2);
    }

    default void apply(Operation operation,
                       MapMathContext.MapMathContextBuilder<?,?> builder, Numeral ... parameters) {
        apply(operation, builder.build(), parameters);
    }

    default void apply(Operation operation, Numeral ... parameters) {
        apply(operation, MathContext.defaults(), parameters);
    }

    default void apply(Operation operation, MapMathContext.MapMathContextBuilder<?,?> builder,
                       Collection<Handle> handles, Numeral ... parameters) {
        apply(operation, builder.build(), handles, parameters);
    }

    default void apply(Operation operation, Collection<Handle> handles, Numeral ... parameters) {
        apply(operation, MapMathContext.defaults(), handles, parameters);
    }

    default void apply(Operation operation, MapMathContext.MapMathContextBuilder<?,?> builder,
                       Map<Handle, Numeral[]> parameters) {
        apply(operation, builder.build(), parameters);
    }

    default void apply(Operation operation, Map<Handle, Numeral[]> parameters) {
        apply(operation, MapMathContext.defaults(), parameters);
    }

    default boolean compare(Comparison comparison, ComparisonContext.ComparisonContextBuilder<?,?> builder,
                            Numeral other) {
        return compare(comparison, builder.build(), other);
    }

    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }

    default boolean compare(Comparison comparison, MapComparisonContext.MapComparisonContextBuilder<?,?> builder,
                            Collection<Handle> handles, Numeral other) {
        return compare(comparison, builder.build(), handles, other);
    }

    default boolean compare(Comparison comparison, Collection<Handle> handles, Numeral other) {
        return compare(comparison, MapComparisonContext.defaults(), handles, other);
    }

    default boolean compare(Comparison comparison, MapComparisonContext.MapComparisonContextBuilder<?,?> builder,
                            Map<Handle, Numeral> others) {
        return compare(comparison, builder.build(), others);
    }

    default boolean compare(Comparison comparison, Map<Handle, Numeral> others) {
        return compare(comparison, MapComparisonContext.defaults(), others);
    }
}
