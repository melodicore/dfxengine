package me.datafox.dfxengine.values;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
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
     * @param value {@link Numeral} to initialize this value with
     */
    public StaticValue(Numeral value) {
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
     * This implementation always returns {@code true}.
     *
     * @return {@code true} if this value is static
     */
    @Override
    public boolean isImmutable() {
        return true;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param type ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return false;
    }

    /**
     * This implementation always throws {@link UnsupportedOperationException}.
     *
     * @param type ignored parameter
     * @return nothing
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public boolean convert(NumeralType type) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param type ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean convertIfAllowed(NumeralType type) {
        return false;
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @return nothing
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public boolean toInteger() {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @return nothing
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public boolean toDecimal() {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @return {@code false}
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
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void set(Numeral value) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameter ignored parameter
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameter1 ignored parameter
     * @param parameter2 ignored parameter
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        throw new UnsupportedOperationException(IMMUTABLE);
    }

    /**
     * This implementation will always throw {@link UnsupportedOperationException}.
     *
     * @param operation ignored parameter
     * @param context ignored parameter
     * @param parameters ignored parameter
     *
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        throw new UnsupportedOperationException(IMMUTABLE);
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
     * @return {@link Set#of()}
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Set.of();
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean addModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean removeModifiers(Collection<? extends Modifier> modifiers) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifier ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param modifiers ignored parameter
     * @return {@code false}
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
     * @return {@link Set#of()}
     */
    @Override
    public Collection<Dependent> getDependents() {
        return Set.of();
    }

    /**
     * Static values do not need to be invalidated.
     */
    @Override
    public void invalidateDependents() {

    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependent ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean addDependent(Dependent dependent) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependencies ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean addDependents(Collection<? extends Dependent> dependencies) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependent ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean removeDependent(Dependent dependent) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependents ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean removeDependents(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependent ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean containsDependent(Dependent dependent) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependents ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean containsDependents(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependent ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean containsDependentRecursive(Dependent dependent) {
        return false;
    }

    /**
     * This implementation will always return {@code false}.
     *
     * @param dependents ignored parameter
     * @return {@code false}
     */
    @Override
    public boolean containsDependentsRecursive(Collection<? extends Dependent> dependents) {
        return false;
    }

    /**
     * This implementation will always return an empty {@link Stream}.
     *
     * @return {@link Stream#empty()}
     */
    @Override
    public Stream<Dependent> dependentStream() {
        return Stream.empty();
    }

    /**
     * This implementation will always return an empty {@link Stream}.
     *
     * @return {@link Stream#empty()}
     */
    @Override
    public Stream<Dependent> recursiveDependentStream() {
        return Stream.empty();
    }

    /**
     * @return {@link String} representation of this value in format <i>StaticValue(value)</i>
     */
    @Override
    public String toString() {
        return String.format("StaticValue(%s)", getValue());
    }
}
