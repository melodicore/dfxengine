package me.datafox.dfxengine.math.value;

import lombok.Data;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.Value;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.modifier.Modifier;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;
import me.datafox.dfxengine.math.utils.Conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
public class StaticValue implements Value {
    private final Numeral value;

    public StaticValue(int i) {
        this.value = Conversion.toNumeral(i);
    }

    public StaticValue(long l) {
        this.value = Conversion.toNumeral(l);
    }

    public StaticValue(BigInteger bi) {
        this.value = Conversion.toNumeral(bi);
    }

    public StaticValue(float f) {
        this.value = Conversion.toNumeral(f);
    }

    public StaticValue(double d) {
        this.value = Conversion.toNumeral(d);
    }

    public StaticValue(BigDecimal bd) {
        this.value = Conversion.toNumeral(bd);
    }

    @Override
    public Numeral getBase() {
        return value;
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
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public void toSmallestType() {
        throw new UnsupportedOperationException("static value cannot be changed");}

    @Override
    public void set(Numeral value, MathContext context) {
        throw new UnsupportedOperationException("static value cannot be changed");
    }

    @Override
    public void apply(SourceOperation operation, MathContext context) {

    }

    @Override
    public void apply(SingleParameterOperation operation, Numeral parameter, MathContext context) {

    }

    @Override
    public void apply(Operation operation, List<Numeral> parameters, MathContext context) {

    }

    @Override
    public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
        return false;
    }

    @Override
    public Collection<Modifier> getModifiers() {
        return null;
    }

    @Override
    public boolean addModifier(Modifier modifier) {
        return false;
    }

    @Override
    public boolean addModifiers(Collection<Modifier> modifiers) {
        return false;
    }

    @Override
    public boolean removeModifier(Modifier modifier) {
        return false;
    }

    @Override
    public boolean removeModifiers(Collection<Modifier> modifiers) {
        return false;
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
        return null;
    }

    @Override
    public void invalidateDependencies() {

    }

    @Override
    public boolean addDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean addDependencies(Collection<Dependency> dependencies) {
        return false;
    }

    @Override
    public boolean removeDependency(Dependency dependency) {
        return false;
    }

    @Override
    public boolean removeDependencies(Collection<Dependency> dependencies) {
        return false;
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
        return null;
    }

    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return null;
    }

    @Override
    public Handle getHandle() {
        return null;
    }
}
