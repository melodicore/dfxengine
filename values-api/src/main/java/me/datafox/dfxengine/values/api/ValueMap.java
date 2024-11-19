package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Space;
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
 * An extension of {@link HandleMap} for storing and manipulating multiple {@link Value Values} simultaneously. A
 * value map may only contain either only mutable Values or only immutable Values. A value map may not contain static
 * Values.
 *
 * @author datafox
 */
public interface ValueMap extends HandleMap<Value> {
    /**
     * Returns {@code true} if this map is for immutable {@link Value Values}.
     *
     * @return {@code true} if this map is for immutable {@link Value Values}
     */
    boolean isImmutable();

    /**
     * Converts all {@link Value Values} in this map to the specified type.
     *
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the {@link Value Values} in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void convert(NumeralType type);

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified type.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the {@link Value Values} to be converted
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void convert(Collection<? extends Handle> handles, NumeralType type);

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified types.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param types map of {@link Handle Handles} and {@link NumeralType NumeralTypes} for the {@link Value Values}
     * represented by those Handles to be converted to.
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void convert(Map<? extends Handle, NumeralType> types);

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified type.
     * Only converts {@link Value Values} of this map that can be converted to the specified type. In other words, calls
     * {@link Numeral#convertIfAllowed(NumeralType)} on every Value of this map.
     *
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    void convertAllowed(NumeralType type);

    /**
     * Converts all {@link Value Values} to the smallest integer type that can hold its represented value. Values that
     * are already integers are not converted.
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void toInteger();

    /**
     * Converts all {@link Value Values} to the smallest decimal type that can hold its represented value. Values that
     * are already decimals are not converted.
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
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
     * Sets all {@link Value Values} of this map to the specified value.
     *
     * @param value {@link Numeral} for all {@link Value Values} of this map to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void set(Numeral value);

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified value.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void set(MapMathContext context, Collection<? extends Handle> handles, Numeral value);

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified values.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void set(MapMathContext context, Map<? extends Handle, Numeral> values);

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(SourceOperation operation, MathContext context);

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(SourceOperation operation, MapMathContext context, Collection<? extends Handle> handles);

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(SingleParameterOperation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral parameter);

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(SingleParameterOperation operation, MapMathContext context, Map<? extends Handle, Numeral> parameters);

    /**
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    /**
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link DualParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(DualParameterOperation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral parameter1, Numeral parameter2);

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(Operation operation, MathContext context, Numeral ... parameters);

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(Operation operation, MapMathContext context,
               Collection<? extends Handle> handles, Numeral ... parameters);

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    void apply(Operation operation, MapMathContext context, Map<? extends Handle, Numeral[]> parameters);

    /**
     * Compares all {@link Value Values} in this map to a {@link Numeral} using the specified {@link Comparison}.
     *
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    /**
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to a {@link Numeral}
     * using the specified {@link Comparison}.
     *
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
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified
     * {@link Numeral Numerals} using the specified {@link Comparison}.
     *
     * @param comparison {@link Comparison} to be used
     * @param context {@link MapComparisonContext} for the comparison
     * @param others map of {@link Handle Handles} of the {@link Value Values} to be compared and
     * {@link Numeral Numerals} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    boolean compare(Comparison comparison, MapComparisonContext context, Map<? extends Handle, Numeral> others);

    /**
     * Returns a {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values. The returned map does not have any state and instead provides a view to this map.
     *
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values.
     */
    Map<Handle, Numeral> getBaseNumeralMap();

    /**
     * Returns a {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()}
     * as the map values. The returned map does not have any state and instead provides a view to this map.
     *
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()} as
     * the map values.
     */
    Map<Handle, Numeral> getValueNumeralMap();

    /**
     * Returns the {@link Modifier Modifiers} associated with this map.
     *
     * @return {@link Modifier Modifiers} associated with this map
     */
    Collection<Modifier> getModifiers();

    /**
     * Registers a {@link Modifier} to this map. Modifiers added to this map will be added to all {@link Value Values}
     * of this map, including ones that are added to this map after this operation. To add a modifier to a single value,
     * use {@link Value#addModifier(Modifier)} instead.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean addModifier(Modifier modifier);

    /**
     * Registers {@link Modifier Modifiers} to this map. Modifiers added to this map will be added to all
     * {@link Value Values} of this map, including ones that are added to this map after this operation. To add
     * modifiers to a single value, use {@link Value#addModifiers(Collection)}} instead.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean addModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Removes a {@link Modifier} from this map.
     *
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean removeModifier(Modifier modifier);

    /**
     * Removes {@link Modifier Modifiers} from this map.
     *
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    boolean removeModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Checks if a {@link Modifier} is present in this map.
     *
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this map
     */
    boolean containsModifier(Modifier modifier);

    /**
     * Checks if {@link Modifier Modifiers} are present in this map.
     *
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this map
     */
    boolean containsModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified value.
     * Handles that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     */
    default void set(Collection<? extends Handle> handles, Numeral value) {
        set(MapMathContext.defaults(), handles, value);
    }

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified values.
     * Handles that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     */
    default void set(Map<? extends Handle, Numeral> values) {
        set(MapMathContext.defaults(), values);
    }

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map. Uses {@link MathContext#defaults()}
     * for context.
     *
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     */
    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored. Uses
     * {@link MapMathContext#defaults()} for context.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     */
    default void apply(SourceOperation operation, Collection<? extends Handle> handles) {
        apply(operation, MapMathContext.defaults(), handles);
    }

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map. Uses
     * {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param parameter parameter for the operation
     */
    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored. Uses
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
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored. Uses
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
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map. Uses
     * {@link MathContext#defaults()} for context.
     *
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    /**
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored. Uses
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
     * Applies an {@link Operation} to all {@link Value Values} of this map. Uses {@link MathContext#defaults()} for
     * context.
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
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored. Uses
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
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored. Uses
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
     * Compares all {@link Value Values} in this map to a {@link Numeral} using the specified {@link Comparison}. Uses
     * {@link ComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }

    /**
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to a {@link Numeral}
     * using the specified {@link Comparison}. Uses {@link MapComparisonContext#defaults()} for context.
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
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified
     * {@link Numeral Numerals} using the specified {@link Comparison}. Uses {@link MapComparisonContext#defaults()} for
     * context.
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
