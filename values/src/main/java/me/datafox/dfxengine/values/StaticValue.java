package me.datafox.dfxengine.values;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;
import me.datafox.dfxengine.values.modifier.AbstractModifier;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

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

    protected StaticValue(Numeral value) {
        this.value = value;
    }

    /**
     * This implementation always returns {@code null}
     *
     * @return {@link Handle} associated with this value, or {@code null} if this value is static
     */
    @Override
    public Handle getHandle() {
        return null;
    }

    /**
     * @return {@link Numeral} value of this value
     */
    @Override
    public Numeral getBase() {
        return value;
    }

    /**
     * @return {@link Numeral} value of this value
     */
    @Override
    public Numeral getValue() {
        return value;
    }

    /**
     * This implementation always returns {@code true}.
     *
     * @return {@code true} if this value is static
     */
    @Override
    public boolean isStatic() {
        return true;
    }

    /**
     * This implementation only returns {@code true} if the {@link Numeral} value has the specified {@link NumeralType}.
     *
     * @param type {@link NumeralType} to be checked for
     * @return {@code true} if the {@link Numeral} value of this value can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return value.getType().equals(type);
    }

    /**
     * This implementation always throws {@link UnsupportedOperationException} unless the {@link Numeral} value has the
     * specified {@link NumeralType}.
     *
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws ExtendedArithmeticException if the base {@link Numeral} of this value is outside the specified type's
     * bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this value is static and the specified type is not the same as the
     * {@link Numeral} value's type
     */
    @Override
    public boolean convert(NumeralType type) {
        if(value.getType().equals(type)) {
            return false;
        }
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws NullPointerException if the specified type is {@code null}
     *
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean convertIfAllowed(NumeralType type) {
        return false;
    }

    /**
     * This implementation will throw {@link UnsupportedOperationException} if the {@link Numeral} value is not an
     * integer.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is static and the {@link Numeral} value is not an integer
     */
    @Override
    public boolean toInteger() {
        if(value.getType().isInteger()) {
            return false;
        }

        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will throw {@link UnsupportedOperationException} if the {@link Numeral} value is not a
     * decimal.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     *
     * @throws UnsupportedOperationException if this value is static and the {@link Numeral} value is not a decimal
     */
    @Override
    public boolean toDecimal() {
        if(value.getType().isDecimal()) {
            return false;
        }

        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    @Override
    public boolean toSmallestType() {
        return false;
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param value ignored parameter
     *
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void set(Numeral value) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     *
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameter ignored parameter
     *
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameter1 ignored parameter
     * @param parameter1 ignored parameter
     *
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameters ignored parameter
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this value is static
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
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
     * This implementation will always return an empty collection.
     *
     * @return {@link Modifier Modifiers} associated with this value
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Set.of();
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this value
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this value
     */
    @Override
    public boolean containsModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * Static values do not need to be invalidated.
     */
    @Override
    public void invalidate() {}

    /**
     * This implementation will always return an empty collection.
     *
     * @return all {@link Dependency Dependencies} that depend on this value
     */
    @Override
    public Collection<Dependency> getDependencies() {
        return Set.of();
    }

    /**
     * Static values do not need to be invalidated.
     */
    @Override
    public void invalidateDependencies() {

    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependency {@link Dependency} that depends on this value
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependency(Dependency dependency) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependencies {@link Dependency Dependencies} that depend on this value
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependency {@link Dependency} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    @Override
    public boolean removeDependency(Dependency dependency) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependencies {@link Dependency Dependencies} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    @Override
    public boolean removeDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this value
     */
    @Override
    public boolean containsDependency(Dependency dependency) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if all the specified {@link Dependency Dependencies} are registered to this value
     */
    @Override
    public boolean containsDependencies(Collection<? extends Dependency> dependencies) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this value or any of its Dependencies
     * that also implement Dependent, recursively
     */
    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependency Dependencies} are registered to this value or any of its
     * Dependencies that also implement Dependent, recursively
     */
    @Override
    public boolean containsDependenciesRecursive(Collection<? extends Dependency> dependencies) {
        return false;
    }

    /**
     * This implementation will always return an empty {@link Stream}.
     *
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this value
     */
    @Override
    public Stream<Dependency> dependencyStream() {
        return Stream.empty();
    }

    /**
     * This implementation will always return an empty {@link Stream}.
     *
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this value or any of its
     * Dependencies that also implement Dependent, recursively
     */
    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return Stream.empty();
    }

    /**
     * @return {@link String} representation of this value in format <i>StaticValue(value)</i>
     */
    @Override
    public String toString() {
        return String.format("StaticValue(%s)", getValue());
    }

    /**
     * @param numeral {@link Numeral} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(Numeral numeral) {
        return new StaticValue(numeral);
    }

    /**
     * @param i {@code int} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(int i) {
        return new StaticValue(Numerals.valueOf(i));
    }

    /**
     * @param l {@code long} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(long l) {
        return new StaticValue(Numerals.valueOf(l));
    }

    /**
     * @param bi {@link BigInteger} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(BigInteger bi) {
        return new StaticValue(Numerals.valueOf(bi));
    }

    /**
     * @param f {@code float} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(float f) {
        return new StaticValue(Numerals.valueOf(f));
    }

    /**
     * @param d {@code double} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(double d) {
        return new StaticValue(Numerals.valueOf(d));
    }

    /**
     * @param bd {@link BigDecimal} to initialize this value with
     * @return static value with the specified value
     */
    public static StaticValue of(BigDecimal bd) {
        return new StaticValue(Numerals.valueOf(bd));
    }

    /**
     * @param str {@link String} to initialize this value with
     * @return static value with the specified value
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static StaticValue of(String str) {
        return new StaticValue(Numerals.valueOf(str));
    }
}
