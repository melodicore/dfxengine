package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.comparison.MapComparisonContext;
import me.datafox.dfxengine.math.api.operation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public interface ValueMap extends HandleMap<Value> {
    void convert(NumeralType type) throws ArithmeticException;

    void convert(Collection<Handle> handles, NumeralType type) throws ArithmeticException;

    void convert(Map<Handle, NumeralType> types) throws ArithmeticException;

    void convertAllowed(NumeralType type);

    void toSmallestType();

    void toSmallestType(Collection<Handle> handles);

    void set(Numeral value, MathContext context);

    void set(Collection<Handle> handles, Numeral value, MapMathContext context);

    void set(Map<Handle, Numeral> values, MapMathContext context);

    void apply(SourceOperation operation, MathContext context);

    void apply(Collection<Handle> handles, SourceOperation operation, MapMathContext context);

    void apply(SingleParameterOperation operation, Numeral parameter, MathContext context);

    void apply(Collection<Handle> handles, SingleParameterOperation operation, Numeral parameter, MapMathContext context);

    void apply(SingleParameterOperation operation, Map<Handle, Numeral> parameters, MapMathContext context);

    void apply(Operation operation, List<Numeral> parameters, MathContext context);

    void apply(Collection<Handle> handles, Operation operation, List<Numeral> parameters, MapMathContext context);

    void apply(Operation operation, Map<Handle, List<Numeral>> parameters, MapMathContext context);

    boolean compare(Comparison comparison, Numeral other, ComparisonContext context);

    boolean compare(Collection<Handle> handles, Comparison comparison, Numeral other, MapComparisonContext context);

    boolean compare(Comparison comparison, Map<Handle, Numeral> others, MapComparisonContext context);

    Map<Handle, Numeral> getBaseNumeralMap();

    Map<Handle, Numeral> getValueNumeralMap();

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

    default void set(Collection<Handle> handles, Numeral value) {
        set(handles, value, MapMathContext.getDefaults());
    }

    default void set(Map<Handle, Numeral> values) {
        set(values, MapMathContext.getDefaults());
    }

    default void apply(SourceOperation operation) {
        apply(operation, MathContext.getDefaults());
    }

    default void apply(Collection<Handle> handles, SourceOperation operation) {
        apply(handles, operation, MapMathContext.getDefaults());
    }

    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, parameter, MathContext.getDefaults());
    }

    default void apply(Collection<Handle> handles, SingleParameterOperation operation, Numeral parameter) {
        apply(handles, operation, parameter, MapMathContext.getDefaults());
    }

    default void apply(SingleParameterOperation operation, Map<Handle, Numeral> parameters) {
        apply(operation, parameters, MapMathContext.getDefaults());
    }

    default void apply(Operation operation, List<Numeral> parameters) {
        apply(operation, parameters, MathContext.getDefaults());
    }

    default void apply(Collection<Handle> handles, Operation operation, List<Numeral> parameters) {
        apply(handles, operation, parameters, MapMathContext.getDefaults());
    }

    default void apply(Operation operation, Map<Handle, List<Numeral>> parameters) {
        apply(operation, parameters, MapMathContext.getDefaults());
    }

    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, other, ComparisonContext.getDefaults());
    }

    default boolean compare(Collection<Handle> handles, Comparison comparison, Numeral other) {
        return compare(handles, comparison, other, MapComparisonContext.getDefaults());
    }

    default boolean compare(Comparison comparison, Map<Handle, Numeral> others) {
        return compare(comparison, others, MapComparisonContext.getDefaults());
    }
}
