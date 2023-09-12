package me.datafox.dfxengine.values;

import lombok.Data;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.Modifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
public class StaticValue implements Value {
    private final Numeral value;

    protected StaticValue(Numeral value) {
        this.value = value;
    }

    public StaticValue(int i) {
        this(Numerals.valueOf(i));
    }

    public StaticValue(long l) {
        this(Numerals.valueOf(l));
    }

    public StaticValue(BigInteger bi) {
        this(Numerals.valueOf(bi));
    }

    public StaticValue(float f) {
        this(Numerals.valueOf(f));
    }

    public StaticValue(double d) {
        this(Numerals.valueOf(d));
    }

    public StaticValue(BigDecimal bd) {
        this(Numerals.valueOf(bd));
    }

    @Override
    public Numeral getBase() {
        return value;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public void convert(NumeralType type) {}

    @Override
    public boolean convertIfAllowed(NumeralType type) {
        return false;
    }

    @Override
    public boolean convertToDecimal() {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public boolean canConvert(NumeralType type) {
        return false;
    }

    @Override
    public void toSmallestType() {}

    @Override
    public void set(Numeral value, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public void apply(SourceOperation operation, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public void apply(SingleParameterOperation operation, Numeral parameter, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public void apply(Operation operation, List<Numeral> parameters, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
        return comparison.compare(value, other);
    }

    @Override
    public Collection<Modifier> getModifiers() {
        return Set.of();
    }

    @Override
    public boolean addModifier(Modifier modifier) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean addModifiers(Collection<Modifier> modifiers) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean removeModifier(Modifier modifier) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean removeModifiers(Collection<Modifier> modifiers) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean containsModifier(Modifier modifier) {
        return false;
    }

    @Override
    public boolean containsModifiers(Collection<Modifier> modifiers) {
        return false;
    }

    @Override
    public void invalidate() {

    }

    @Override
    public Collection<Dependency> getDependencies() {
        return Set.of();
    }

    @Override
    public void invalidateDependencies() {

    }

    @Override
    public boolean addDependency(Dependency dependency) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean addDependencies(Collection<Dependency> dependencies) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean removeDependency(Dependency dependency) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean removeDependencies(Collection<Dependency> dependencies) {
        throw new UnsupportedOperationException("static value cannot be modified");
    }

    @Override
    public boolean containsDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean containsDependencies(Collection<Dependency> dependencies) {
        return false;
    }

    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return false;
    }

    @Override
    public boolean containsDependenciesRecursive(Collection<Dependency> dependencies) {
        return false;
    }

    @Override
    public Stream<Dependency> dependencyStream() {
        return Stream.empty();
    }

    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return Stream.empty();
    }

    @Override
    public Handle getHandle() {
        return null;
    }
}
