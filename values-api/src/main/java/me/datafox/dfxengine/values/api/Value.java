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
 * {@link Modifier Modifiers}. Each value contains a base Numeral that can be changed with {@link #set(Numeral)} and
 * {@link #apply(Operation, MathContext, Numeral...)} and other {@code apply(...)} methods. Each value also contains a
 * value Numeral, which represents the base Numeral with all the Modifiers applied to it. This value should be lazily
 * calculated when {@link #getValue()} is called, but only when the base Numeral or any of the Modifiers have changed.
 * It is recommended for the implementation to extend {@link DependencyDependent} to simplify invalidating the cached
 * value.
 * </p>
 * <p>
 * Values can also be considered static. A static value should be immutable, unmodifiable and not identified by a
 * Handle. More specifically, {@link #getHandle()} should return {@code null}, any calls to {@link #set(Numeral)} and
 * all {@code apply(...)} methods should throw {@link UnsupportedOperationException}, and all Modifier-related methods
 * should return {@code false} with no other action.
 * </p>
 *
 * @author datafox
 */
public interface Value extends Dependency, Dependent, Handled {
    /**
     * @return {@link Handle} associated with this value, or {@code null} if this value is static
     */
    @Override
    Handle getHandle();

    /**
     * @return the base {@link Numeral} of this value
     */
    Numeral getBase();

    /**
     * @return the base {@link Numeral} of this value with all {@link Modifier Modifiers} of this value applied to it
     */
    Numeral getValue();

    /**
     * @return {@code true} if this value is static
     */
    boolean isStatic();

    /**
     * @param type type to be checked for
     * @return {@code true} if the base {@link Numeral} of this value can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    boolean canConvert(NumeralType type);

    /**
     * @param type type for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws ExtendedArithmeticException if the base {@link Numeral} of this value is outside the specified type's
     * bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    boolean convert(NumeralType type);

    /**
     * @param type type for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    boolean convertIfAllowed(NumeralType type);

    /**
     * If the base {@link Numeral} does not represent an integer value ({@link NumeralType#isInteger()} is {@code false}
     * for {@link Numeral#getType()}), converts it to the smallest integer type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    boolean toInteger();

    /**
     * If the base {@link Numeral} does not represent a decimal value ({@link NumeralType#isDecimal()} is {@code false}
     * for {@link Numeral#getType()}), converts it to the smallest decimal type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
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
     * @param value value to replace the current base {@link Numeral}
     */
    void set(Numeral value);

    /**
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     */
    void apply(SourceOperation operation, MathContext context);

    /**
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     */
    void apply(SingleParameterOperation operation, MathContext context, Numeral parameter);

    /**
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter1 second parameter for the operation
     */
    void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2);

    /**
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    void apply(Operation operation, MathContext context, Numeral ... parameters);

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    boolean compare(Comparison comparison, ComparisonContext context, Numeral other);

    /**
     * @return {@link Modifier Modifiers} associated with this value
     */
    Collection<Modifier> getModifiers();

    /**
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean addModifier(Modifier modifier);

    /**
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean addModifiers(Collection<? extends Modifier> modifiers);

    /**
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean removeModifier(Modifier modifier);

    /**
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    boolean removeModifiers(Collection<? extends Modifier> modifiers);

    /**
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this value
     */
    boolean containsModifier(Modifier modifier);

    /**
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this value
     */
    boolean containsModifiers(Collection<? extends Modifier> modifiers);

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     */
    default void apply(SourceOperation operation) {
        apply(operation, MathContext.defaults());
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param parameter parameter for the operation
     */
    default void apply(SingleParameterOperation operation, Numeral parameter) {
        apply(operation, MathContext.defaults(), parameter);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param parameter1 first parameter for the operation
     * @param parameter1 second parameter for the operation
     */
    default void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2) {
        apply(operation, MathContext.defaults(), parameter1, parameter2);
    }

    /**
     * Uses {@link MathContext#defaults()} for context.
     *
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    default void apply(Operation operation, Numeral ... parameters) {
        apply(operation, MathContext.defaults(), parameters);
    }

    /**
     * Uses {@link ComparisonContext#defaults()} for context.
     *
     * @param comparison {@link Comparison} to be used
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    default boolean compare(Comparison comparison, Numeral other) {
        return compare(comparison, ComparisonContext.defaults(), other);
    }
}
