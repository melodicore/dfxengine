package me.datafox.dfxengine.values;

import lombok.Data;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A static implementation of
 *
 * @author datafox
 */
@Data
public class StaticValue implements Value {
    private final Numeral value;

    protected StaticValue(Numeral value) {
        this.value = value;
    }

    @Override
    public Numeral getBase() {
        return value;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    /**
     * @implNote operation not supported, will throw an exception if the backing {@link Numeral Numeral's} type is not
     * equal to the specified type
     */
    @Override
    public boolean convert(NumeralType type) {
        if(value.getType().equals(type)) {
            return false;
        }
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * @implNote operation not supported, will return false
     */
    @Override
    public boolean convertIfAllowed(NumeralType type) {
        return false;
    }

    /**
     * @implNote operation not supported, will throw an exception if the backing {@link Numeral} has an integer type
     */
    @Override
    public boolean convertToDecimal() {
        if(value.getType().isDecimal()) {
            return false;
        }
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean canConvert(NumeralType type) {
        return false;
    }

    @Override
    public void toSmallestType() {}

    /**
     * @implNote operation not supported, will throw an exception
     */
    @Override
    public void set(Numeral value, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * @implNote operation not supported, will throw an exception
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * @implNote operation not supported, will throw an exception
     */
    @Override
    public void apply(SingleParameterOperation operation, Numeral parameter, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * @implNote operation not supported, will throw an exception
     */
    @Override
    public void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    /**
     * @implNote operation not supported, will throw an exception
     */
    @Override
    public void apply(Operation operation, List<Numeral> parameters, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
        return comparison.compare(value, other);
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return an empty {@link Set}
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Set.of();
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        return false;
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean addModifiers(Collection<Modifier> modifiers) {
        return false;
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        return false;
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean removeModifiers(Collection<Modifier> modifiers) {
        return false;
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return false;
    }

    /**
     * @implNote {@link Modifier Modifiers} not supported, will return false
     */
    @Override
    public boolean containsModifiers(Collection<Modifier> modifiers) {
        return false;
    }

    @Override
    public void invalidate() {

    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return an empty {@link Set}
     */
    @Override
    public Collection<Dependency> getDependencies() {
        return Set.of();
    }

    @Override
    public void invalidateDependencies() {

    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean addDependency(Dependency dependency) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean addDependencies(Collection<Dependency> dependencies) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean removeDependency(Dependency dependency) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean removeDependencies(Collection<Dependency> dependencies) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean containsDependency(Dependency dependency) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean containsDependencies(Collection<Dependency> dependencies) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return false
     */
    @Override
    public boolean containsDependenciesRecursive(Collection<Dependency> dependencies) {
        return false;
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return an empty {@link Stream}
     */
    @Override
    public Stream<Dependency> dependencyStream() {
        return Stream.empty();
    }

    /**
     * @implNote {@link Dependency Dependencies} not supported, will return an empty {@link Stream}
     */
    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return Stream.empty();
    }

    /**
     * @implNote {@link Handle Handles} not supported, will return null
     */
    @Override
    public Handle getHandle() {
        return null;
    }

    public static StaticValue of(Numeral numeral) {
        return new StaticValue(numeral);
    }

    public static StaticValue of(int i) {
        return new StaticValue(Numerals.valueOf(i));
    }

    public static StaticValue of(long l) {
        return new StaticValue(Numerals.valueOf(l));
    }

    public static StaticValue of(BigInteger bi) {
        return new StaticValue(Numerals.valueOf(bi));
    }

    public static StaticValue of(float f) {
        return new StaticValue(Numerals.valueOf(f));
    }

    public static StaticValue of(double d) {
        return new StaticValue(Numerals.valueOf(d));
    }

    public static StaticValue of(BigDecimal bd) {
        return new StaticValue(Numerals.valueOf(bd));
    }

    public static StaticValue of(String str) {
        return new StaticValue(Numerals.valueOf(str));
    }
}
