package me.datafox.dfxengine.values;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;
import me.datafox.dfxengine.values.modifier.AbstractModifier;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.IMMUTABLE;

/**
 * A numeric value backed with a {@link Numeral}. A static value is immutable, unmodifiable and not identified by a
 * Handle. More specifically, {@link #getHandle()} return {@code null}, any calls to {@link #set(Numeral)} and all
 * {@code apply(...)} methods throw {@link UnsupportedOperationException}, and all Modifier-related methods return
 * {@code false} with no other action. Static values are primarily meant to be used as immutable parameters for
 * {@link Modifier} implementations in this module ({@link AbstractModifier}, {@link OperationModifier} and
 * {@link MappingOperationModifier}).
 *
 * @author datafox
 */
@EqualsAndHashCode
public class StaticValue implements Value {
    private final Numeral value;

    /**
     * Public constructor for {@link StaticValue}.
     *
     * @param value {@link Numeral} to initialize this value with
     */
    public StaticValue(Numeral value) {
        this.value = value;
    }

    /**
     * Returns the {@link Handle} associated with this value. This implementation always returns {@code null}.
     *
     * @return {@link Handle} associated with this value, or {@code null} if this value is static
     */
    @Override
    public Handle getHandle() {
        return null;
    }

    /**
     * Returns the {@link Numeral} of this value.
     *
     * @return {@link Numeral} of this value
     */
    @Override
    public Numeral getBase() {
        return value;
    }

    /**
     * Returns the {@link Numeral} of this value.
     *
     * @return {@link Numeral} of this value
     */
    @Override
    public Numeral getValue() {
        return value;
    }

    /**
     * Returns {@code true} if this value is static. This implementation always returns {@code true}.
     *
     * @return {@code true} if this value is static
     */
    @Override
    public boolean isStatic() {
        return true;
    }

    /**
     * Returns {@code true} if this value is immutable. Static values are always immutable. This implementation always
     * returns {@code true}.
     *
     * @return {@code true} if this value is immutable
     */
    @Override
    public boolean isImmutable() {
        return true;
    }

    /**
     * Returns {@code true} if the base {@link Numeral} of this value can be converted to the specified type. This
     * implementation always returns {@code false}.
     *
     * @param type {@link NumeralType} to be checked for
     * @return {@code true} if the base {@link Numeral} of this value can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return false;
    }

    /**
     * Converts the base {@link Numeral} to the specified type, or throws an exception if the value is outside the
     * specified type's bounds. This implementation always throws {@link UnsupportedOperationException}.
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
    @Override
    public boolean convert(NumeralType type) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Converts the base {@link Numeral} to the specified type, or does nothing if the conversion would throw an
     * {@link ExtendedArithmeticException}. This implementation always returns {@code false}.
     *
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean convertIfAllowed(NumeralType type) {
        return false;
    }

    /**
     * Converts the base {@link Numeral} to an integer value. If the base {@link Numeral} does not represent an integer
     * value ({@link NumeralType#isInteger()} is {@code false} for {@link Numeral#getType()}), converts it to the
     * smallest integer type that hold the represented value. This implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public boolean toInteger() {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Converts the base {@link Numeral} to a decimal value. If the base {@link Numeral} does not represent a decimal
     * value ({@link NumeralType#isDecimal()} is {@code false} for {@link Numeral#getType()}), converts it to the
     * smallest decimal type that hold the represented value. This implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public boolean toDecimal() {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Converts the base {@link Numeral} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations. This implementation always returns {@code false}.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    @Override
    public boolean toSmallestType() {
        return false;
    }

    /**
     * Sets the base {@link Numeral} to the specified value. This implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param value value to replace the current base {@link Numeral}
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public void set(Numeral value) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Applies a {@link SourceOperation} to the base {@link Numeral} of this value. This implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Applies a {@link SingleParameterOperation} to the base {@link Numeral} of this value. This implementation always
     * throws {@link UnsupportedOperationException}.
     *
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Applies a {@link DualParameterOperation} to the base {@link Numeral} of this value. This implementation always
     * throws {@link UnsupportedOperationException}.
     *
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Applies an {@link Operation} to the base {@link Numeral} of this value. This implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this value is immutable
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * Compares this value to a {@link Numeral} using the specified {@link Comparison}.
     *
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    @Override
    public boolean compare(Comparison comparison, ComparisonContext context, Numeral other) {
        return comparison.compare(value, other);
    }

    /**
     * Returns the {@link Modifier Modifiers} associated with this value. This implementation always returns an empty
     * collection.
     *
     * @return {@link Modifier Modifiers} associated with this value
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Set.of();
    }

    /**
     * Registers a {@link Modifier} to this value. This implementation always returns {@code false}.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        return false;
    }

    /**
     * Registers {@link Modifier Modifiers} to this value. This implementation always returns {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * Removes a {@link Modifier} from this value. This implementation always returns {@code false}.
     *
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        return false;
    }

    /**
     * Removes {@link Modifier Modifiers} from this value. This implementation always returns {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * Checks if a {@link Modifier} is present in this value. This implementation always returns {@code false}.
     *
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this value
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return false;
    }

    /**
     * Checks if {@link Modifier Modifiers} are present in this value. This implementation always returns {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this value
     */
    @Override
    public boolean containsModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * <p>
     * Invalidates the class implementing this interface. In practice, this should invalidate the caches of any values
     * that are dependent on values of other classes. Because this method may be called multiple times, it is not
     * recommended to recalculate the cached value in this method, and instead create an invalidated flag that is set
     * in this method, and recalculate the value in its getter method if the flag is set.
     * </p>
     * <p>
     * Static values do not need to be invalidated.
     * </p>
     */
    @Override
    public void invalidate() {}

    /**
     * Returns all {@link Dependent Dependents} that depend on this class. This implementation always returns an empty
     * collection.
     *
     * @return all {@link Dependent Dependents} that depend on this class
     */
    @Override
    public Collection<Dependent> getDependents() {
        return Set.of();
    }

    /**
     * Calls {@link Dependent#invalidate()} on all {@link Dependent Dependents} of this value. Static values do not need
     * to be invalidated.
     */
    @Override
    public void invalidateDependents() {

    }

    /**
     * Register a {@link Dependent} that depends on this value. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected. This implementation will always return {@code false}.
     *
     * @param dependent {@link Dependent} that depends on this value
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependent(Dependent dependent) {
        return false;
    }

    /**
     * Register {@link Dependent Dependents} that depends on this value. The method must check for cyclic dependencies
     * and throw {@link IllegalArgumentException} if one is detected. This implementation will always return
     * {@code false}.
     *
     * @param dependents {@link Dependent Dependents} that depend on this value
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependents(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * Removes a {@link Dependent} from this value. This implementation will always return {@code false}.
     *
     * @param dependent {@link Dependent} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    @Override
    public boolean removeDependent(Dependent dependent) {
        return false;
    }

    /**
     * Removes {@link Dependent Dependents} from this value. This implementation will always return {@code false}.
     *
     * @param dependents {@link Dependent Dependents} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    @Override
    public boolean removeDependents(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * Checks if {@link Dependent Dependents} are present in this value. This implementation will always return
     * {@code false}.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if all the specified {@link Dependent Dependents} are registered to this class
     */
    @Override
    public boolean containsDependent(Dependent dependent) {
        return false;
    }

    /**
     * Checks if {@link Dependent Dependents} are present in this value. This implementation will always return
     * {@code false}.
     *
     * @param dependents {@link Dependent Dependents} to be checked for
     * @return {@code true} if all the specified {@link Dependent Dependents} are registered to this class
     */
    @Override
    public boolean containsDependents(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * Checks if the specified {@link Dependent} is registered to this value or any of its dependents that also
     * implement dependent, recursively. In practice any dependent that depends on this value, directly or indirectly,
     * would cause this method to return {@code true}. This implementation will always return {@code false}.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if the specified {@link Dependent} is registered to this class or any of its dependents that
     * also implement dependent, recursively
     */
    @Override
    public boolean containsDependentRecursive(Dependent dependent) {
        return false;
    }

    /**
     * Checks if all the specified {@link Dependent Dependents} are registered to this value or any of its dependencies
     * that also implement dependent, recursively. In practice any collection of dependencies that depend on this value,
     * directly or indirectly, would cause this method to return {@code true}. This implementation will always return
     * {@code false}.
     *
     * @param dependents {@link Dependent Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependent Dependencies} are registered to this class or any of its
     * dependencies that also implement dependent, recursively
     */
    @Override
    public boolean containsDependentsRecursive(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class. This
     * implementation will always return an empty {@link Stream}.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class
     */
    @Override
    public Stream<Dependent> dependentStream() {
        return Stream.empty();
    }

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively. This implementation will always return an empty
     * {@link Stream}.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively
     */
    @Override
    public Stream<Dependent> recursiveDependentStream() {
        return Stream.empty();
    }

    /**
     * Returns a {@link String} representation of this value in format <i>StaticValue(value)</i>.
     *
     * @return {@link String} representation of this value in format <i>StaticValue(value)</i>
     */
    @Override
    public String toString() {
        return String.format("StaticValue(%s)", getValue());
    }
}
