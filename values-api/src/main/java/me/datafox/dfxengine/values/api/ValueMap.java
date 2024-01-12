package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
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
    /**
     * @param type type for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the {@link Value Values} in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    void convert(NumeralType type) throws ExtendedArithmeticException;

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the {@link Value Values} to be converted
     * @param type type for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    void convert(Collection<? extends Handle> handles, NumeralType type);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param types map of {@link Handle Handles} and types for the {@link Value Values} represented by those Handles
     * to be converted to.
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    void convert(Map<? extends Handle, NumeralType> types);

    /**
     * Only converts {@link Value Values} of this map that can be converted to the specified type. In other words, calls
     * {@link Numeral#convertIfAllowed(NumeralType)} on every Value of this map.
     *
     * @param type type for the {@link Value Values} to be converted to
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    void convertAllowed(NumeralType type);

    /**
     * Converts all {@link Value Values} to the smallest integer type that can hold its represented value. Values that
     * are already integers are not converted.
     */
    void toInteger();

    /**
     * Converts all {@link Value Values} to the smallest decimal type that can hold its represented value. Values that
     * are already decimals are not converted.
     */
    void toDecimal();

    /**
     * Converts all {@link Value Values} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations.
     */
    void toSmallestType();

    /**
     * Converts specified {@link Value Values} to the smallest type that can hold its represented value. Will not
     * convert between integer and decimal representations. {@link Handle Handles} that are not a part of this map's
     * associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the values to be converted
     */
    void toSmallestType(Collection<? extends Handle> handles);

    /**
     * @param value {@link Numeral} for all {@link Value Values} of this map to be set to
     */
    void set(Numeral value);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     */
    void set(MapMathContext context, Collection<? extends Handle> handles, Numeral value);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     */
    void set(MapMathContext context, Map<? extends Handle, Numeral> values);

    /**
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     */
    void apply(SourceOperation operation, MathContext context);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     */
    void apply(SourceOperation operation, MapMathContext context, Collection<? extends Handle> handles);

    /**
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     */
    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter parameter for the operation
     */
    void apply(SingleParameterOperation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral parameter);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     */
    void apply(SingleParameterOperation operation, MapMathContext context, Map<? extends Handle, Numeral> parameters);

    /**
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link DualParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    void apply(DualParameterOperation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral parameter1, Numeral parameter2);

    /**
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    void apply(Operation operation, MathContext context, Numeral ... parameters);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    void apply(Operation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral ... parameters);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    void apply(Operation operation, MapMathContext context, Map<? extends Handle, Numeral[]> parameters);

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link MapComparisonContext} for the comparison
     * @param handles {@link Handle Handles} of the {@link Value Values} to be compared
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    boolean compare(Comparison comparison, MapComparisonContext context,
                    Collection<? extends Handle> handles, Numeral other);

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link MapComparisonContext} for the comparison
     * @param others map of {@link Handle Handles} of the {@link Value Values} to be compared and
     * {@link Numeral Numerals} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    boolean compare(Comparison comparison, MapComparisonContext context, Map<? extends Handle, Numeral> others);

    /**
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values. The returned map is immutable and changes automatically as this map changes
     */
    Map<Handle, Numeral> getBaseNumeralMap();

    /**
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()} as
     * the map values. The returned map is immutable and changes automatically as this map changes
     */
    Map<Handle, Numeral> getValueNumeralMap();

    /**
     * @return {@link Modifier Modifiers} associated with this map
     */
    Collection<Modifier> getModifiers();

    /**
     * {@link Modifier Modifiers} added to this map will be added to all {@link Value Values} of this map, including
     * ones that are added to this map after this operation. To add a modifier to a single value, use
     * {@link Value#addModifier(Modifier)} instead.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean addModifier(Modifier modifier);

    /**
     * {@link Modifier Modifiers} added to this map will be added to all {@link Value Values} of this map, including
     * ones that are added to this map after this operation. To add modifiers to a single value, use
     * {@link Value#addModifiers(Collection)}} instead.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean addModifiers(Collection<? extends Modifier> modifiers);

    /**
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean removeModifier(Modifier modifier);

    /**
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean removeModifiers(Collection<? extends Modifier> modifiers);

    /**
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this map
     */
    boolean containsModifier(Modifier modifier);

    /**
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this map
     */
    boolean containsModifiers(Collection<? extends Modifier> modifiers);

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     */
    default void set(Collection<? extends Handle> handles, Numeral value) {
        set(MapMathContext.defaults(), handles, value);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     */
    default void set(Map<? extends Handle, Numeral> values) {
        set(MapMathContext.defaults(), values);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     */
    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     */
    default void apply(SourceOperation operation, Collection<? extends Handle> handles) {
        apply(operation, MapMathContext.defaults(), handles);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param parameter parameter for the operation
     */
    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter parameter for the operation
     */
    default void apply(SingleParameterOperation operation, Collection<? extends Handle> handles, Numeral parameter) {
        apply(operation, MapMathContext.defaults(), handles, parameter);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     */
    default void apply(SingleParameterOperation operation, Map<? extends Handle, Numeral> parameters) {
        apply(operation, MapMathContext.defaults(), parameters);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link DualParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    default void apply(DualParameterOperation operation, Collection<? extends Handle> handles,
                       Numeral parameter1, Numeral parameter2) {
        apply(operation, MapMathContext.defaults(), handles, parameter1, parameter2);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    default void apply(Operation operation, Numeral ... parameters) {
        apply(operation, MathContext.defaults(), parameters);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    default void apply(Operation operation, Collection<? extends Handle> handles, Numeral ... parameters) {
        apply(operation, MapMathContext.defaults(), handles, parameters);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    default void apply(Operation operation, Map<? extends Handle, Numeral[]> parameters) {
        apply(operation, MapMathContext.defaults(), parameters);
    }

    /**
     * Uses {@link ComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }

    /**
     * Uses {@link MapComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param handles {@link Handle Handles} of the {@link Value Values} to be compared
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    default boolean compare(Comparison comparison, Collection<? extends Handle> handles, Numeral other) {
        return compare(comparison, MapComparisonContext.defaults(), handles, other);
    }

    /**
     * Uses {@link MapComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param others map of {@link Handle Handles} of the {@link Value Values} to be compared and
     * {@link Numeral Numerals} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    default boolean compare(Comparison comparison, Map<? extends Handle, Numeral> others) {
        return compare(comparison, MapComparisonContext.defaults(), others);
    }
}
