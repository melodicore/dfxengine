package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;

import java.util.Collection;

/**
 * <p>
 * A mutable numeric value identified by a {@link Handle} and backed with a {@link Numeral} that supports dynamic
 * {@link Modifier Modifiers}. Each value contains a base Numeral that can be changed with
 * {@link #convert(NumeralType)}, {@link #convertIfAllowed(NumeralType)}, {@link #toInteger()}, {@link #toDecimal()},
 * {@link #set(Numeral)}, {@link #apply(Operation, MathContext, Numeral...)} and other {@code apply(...)} methods. Each
 * value also contains a value Numeral, which represents the base Numeral with all the Modifiers applied to it. This
 * Numeral should be lazily calculated when {@link #getValue()} is called, but only when the base Numeral or any of the
 * Modifiers have changed. It is recommended for the implementation to extend {@link DependencyDependent} to simplify
 * invalidating the cached value.
 * </p>
 * <p>
 * Values can be immutable. The base Numeral of an immutable value can not be changed, but immutable values can still
 * have Modifiers. Calling {@link #convert(NumeralType)}, {@link #toInteger()}, {@link #toDecimal()},
 * {@link #set(Numeral)} or any of the {@code apply(...)} methods should throw {@link UnsupportedOperationException},
 * and calling {@link #convertIfAllowed(NumeralType)} or {@link #toSmallestType()} should return {@code false} with no
 * other action.
 * </p>
 * <p>
 * Values can also be considered static. A static value should be immutable, unmodifiable and not identified by a
 * Handle. In addition to the rules for immutable values, {@link #getHandle()} should return {@code null} and all
 * Modifier-related methods should return {@code false} with no other action.
 * </p>
 *
 * @author datafox
 */
public interface Value extends Dependent, Dependency, Handled {
    /**
     * Returns the {@link Handle} associated with this value.
     *
     * @return {@link Handle} associated with this value, or {@code null} if this value is static
     */
    @Override
    Handle getHandle();

    /**
     * Returns the base {@link Numeral} of this value.
     *
     * @return base {@link Numeral} of this value
     */
    Numeral getBase();

    /**
     * Returns the base {@link Numeral} of this value with all {@link Modifier Modifiers} of this value applied to it.
     *
     * @return base {@link Numeral} of this value with all {@link Modifier Modifiers} of this value applied to it
     */
    Numeral getValue();

    /**
     * Returns {@code true} if this value is static.
     *
     * @return {@code true} if this value is static
     */
    boolean isStatic();

    /**
     * Returns {@code true} if this value is immutable. Static values are always immutable.
     * 
     * @return {@code true} if this value is immutable
     */
    boolean isImmutable();

    /**
     * Returns {@code true} if the base {@link Numeral} of this value can be converted to the specified type.
     *
     * @param type {@link NumeralType} to be checked for
     * @return {@code true} if the base {@link Numeral} of this value can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    boolean canConvert(NumeralType type);

    /**
     * Converts the base {@link Numeral} to the specified type, or throws an exception if the value is outside the
     * specified type's bounds.
     *
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws ExtendedArithmeticException if the base {@link Numeral} of this value is outside the specified type's
     * bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this value is immutable
     */
    boolean convert(NumeralType type);

    /**
     * Converts the base {@link Numeral} to the specified type, or does nothing if the conversion would throw an
     * {@link ExtendedArithmeticException}.
     *
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    boolean convertIfAllowed(NumeralType type);

    /**
     * Converts the base {@link Numeral} to an integer value. If the base {@link Numeral} does not represent an integer
     * value ({@link NumeralType#isInteger()} is {@code false} for {@link Numeral#getType()}), converts it to the
     * smallest integer type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    boolean toInteger();

    /**
     * Converts the base {@link Numeral} to a decimal value. If the base {@link Numeral} does not represent a decimal
     * value ({@link NumeralType#isDecimal()} is {@code false} for {@link Numeral#getType()}), converts it to the
     * smallest decimal type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    boolean toDecimal();

    /**
     * Converts the base {@link Numeral} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    boolean toSmallestType();

    /**
     * Sets the base {@link Numeral} to the specified value.
     *
     * @param value value to replace the current base {@link Numeral}
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    void set(Numeral value);

    /**
     * Applies a {@link SourceOperation} to the base {@link Numeral} of this value.
     *
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    void apply(SourceOperation operation, MathContext context);

    /**
     * Applies a {@link SingleParameterOperation} to the base {@link Numeral} of this value.
     *
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    /**
     * Applies a {@link DualParameterOperation} to the base {@link Numeral} of this value.
     *
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is static
     */
    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    /**
     * Applies an {@link Operation} to the base {@link Numeral} of this value.
     *
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this value is immutable
     */
    void apply(Operation operation, MathContext context, Numeral ... parameters);

    /**
     * Compares this value to a {@link Numeral} using the specified {@link Comparison}.
     *
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    /**
     * Returns the {@link Modifier Modifiers} associated with this value.
     *
     * @return {@link Modifier Modifiers} associated with this value
     */
    Collection<Modifier> getModifiers();

    /**
     * Registers a {@link Modifier} to this value.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean addModifier(Modifier modifier);

    /**
     * Registers {@link Modifier Modifiers} to this value.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean addModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Removes a {@link Modifier} from this value.
     *
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean removeModifier(Modifier modifier);

    /**
     * Removes {@link Modifier Modifiers} from this value.
     *
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean removeModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Checks if a {@link Modifier} is present in this value.
     *
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this value
     */
    boolean containsModifier(Modifier modifier);

    /**
     * Checks if {@link Modifier Modifiers} are present in this value.
     *
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this value
     */
    boolean containsModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Applies a {@link SourceOperation} to the base {@link Numeral} of this value. Uses {@link MathContext#defaults()}
     * for context.
     *
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     *
     * @throws UnsupportedOperationException if this value is static
     */
    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    /**
     * Applies a {@link SingleParameterOperation} to the base {@link Numeral} of this value. Uses
     * {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is static
     */
    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    /**
     * Applies a {@link DualParameterOperation} to the base {@link Numeral} of this value. Uses
     * {@link MathContext#defaults()} for context.
     *
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is static
     */
    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    /**
     * Applies an {@link Operation} to the base {@link Numeral} of this value. Uses {@link MathContext#defaults()} for
     * context.
     *
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this value is static
     */
    default void apply(Operation operation, Numeral ... parameters) {
        apply(operation, MathContext.defaults(), parameters);
    }

    /**
     * Compares this value to a {@link Numeral} using the specified {@link Comparison}. Uses
     * {@link ComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }
}
