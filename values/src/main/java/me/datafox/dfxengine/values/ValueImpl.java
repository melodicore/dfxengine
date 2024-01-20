package me.datafox.dfxengine.values;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.dependencies.Dependent;
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
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * A mutable numeric value identified by a {@link Handle} and backed with a {@link Numeral} that supports dynamic
 * {@link Modifier Modifiers}. Each value contains a base Numeral that can be changed with {@link #set(Numeral)} and
 * {@link #apply(Operation, MathContext, Numeral...)} and other {@code apply(...)} methods. Each value also contains a
 * value Numeral, which represents the base Numeral with all the Modifiers applied to it. This value is lazily
 * calculated when {@link #getValue()} is called, but only when the base Numeral or any of the Modifiers have changed.
 * These changes are tracked with {@link Dependency Dependencies} and {@link Dependent Dependents}.
 *
 * @author datafox
 */
@EqualsAndHashCode(callSuper = false)
public class ValueImpl extends DependencyDependent implements Value {
    private final Handle handle;

    private final SortedSet<Modifier> modifiers;

    private Numeral base;
    private Numeral value;

    private boolean invalidated;

    /**
     * @param handle {@link Handle} identifier for this value
     * @param value {@link Numeral} to initialize this value with
     */
    public ValueImpl(Handle handle, Numeral value) {
        super(LoggerFactory.getLogger(ValueImpl.class));
        this.handle = handle;
        modifiers = new TreeSet<>();
        base = value;
        this.value = value;
        invalidated = false;
    }

    /**
     * @return {@link Handle} associated with this value
     */
    @Override
    public Handle getHandle() {
        return handle;
    }

    /**
     * @return base {@link Numeral} of this value
     */
    @Override
    public Numeral getBase() {
        return base;
    }

    /**
     * @return base {@link Numeral} of this value with all {@link Modifier Modifiers} of this value applied to it
     */
    @Override
    public Numeral getValue() {
        if(invalidated) {
            calculate();
        }

        return value;
    }

    /**
     * This implementation always returns {@code false}.
     *
     * @return {@code true} if this value is static
     */
    @Override
    public boolean isStatic() {
        return false;
    }

    /**
     * @param type {@link NumeralType} to be checked for
     * @return {@code true} if the base {@link Numeral} of this value can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return base.canConvert(type);
    }

    /**
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws ExtendedArithmeticException if the base {@link Numeral} of this value is outside the specified type's
     * bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean convert(NumeralType type) {
        if(base.getType().equals(type)) {
            return false;
        }
        base = base.convert(type);
        invalidate();
        return true;
    }

    /**
     * @param type {@link NumeralType} for the base {@link Numeral} of this value to be converted to
     * @return {@code true} if the base {@link Numeral} of this value was changed as a result of this operation
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean convertIfAllowed(NumeralType type) {
        if(base.getType().equals(type)) {
            return false;
        }
        Numeral old = base;
        base = base.convertIfAllowed(type);
        if(base.equals(old)) {
            return false;
        }
        invalidate();
        return true;
    }

    /**
     * If the base {@link Numeral} does not represent an integer value ({@link NumeralType#isInteger()} is {@code false}
     * for {@link Numeral#getType()}), converts it to the smallest integer type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    @Override
    public boolean toInteger() {
        if(base.getType().isInteger()) {
            return false;
        }
        Numeral old = base;
        base = base.toInteger();
        if(base.equals(old)) {
            return false;
        }
        invalidate();
        return true;
    }

    /**
     * If the base {@link Numeral} does not represent a decimal value ({@link NumeralType#isDecimal()} is {@code false}
     * for {@link Numeral#getType()}), converts it to the smallest decimal type that hold the represented value.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    @Override
    public boolean toDecimal() {
        if(base.getType().isDecimal()) {
            return false;
        }
        Numeral old = base;
        base = base.toDecimal();
        if(base.equals(old)) {
            return false;
        }
        invalidate();
        return true;
    }

    /**
     * Converts the base {@link Numeral} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations.
     *
     * @return {@code true} if the base {@link Numeral} was changed as a result of this operation
     */
    @Override
    public boolean toSmallestType() {
        Numeral old = base;
        base = base.toSmallestType();
        if(base.equals(old)) {
            return false;
        }
        invalidate();
        return true;
    }

    /**
     * @param value value to replace the current base {@link Numeral}
     */
    @Override
    public void set(Numeral value) {
        Numeral old = base;
        base = value;
        if(!base.equals(old)) {
            invalidate();
        }
    }

    /**
     * @param operation {@link SourceOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        contextOperation(() -> base = operation.apply(base), context);
    }

    /**
     * @param operation {@link SingleParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        contextOperation(() -> base = operation.apply(base, parameter), context);
    }

    /**
     * @param operation {@link DualParameterOperation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        contextOperation(() -> base = operation.apply(base, parameter1, parameter2), context);
    }

    /**
     * @param operation {@link Operation} to be applied to the base {@link Numeral}
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        contextOperation(() -> base = operation.apply(base, parameters), context);
    }

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared with
     * @return {@code true} if the specified {@link Comparison} returns {@code true}
     */
    @Override
    public boolean compare(Comparison comparison, ComparisonContext context, Numeral other) {
        return comparison.compare(context.useModifiedValue() ? getValue() : base, other);
    }

    /**
     * @return {@link Modifier Modifiers} associated with this value
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    /**
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        boolean changed = modifiers.add(modifier);
        if(changed) {
            modifier.addDependency(this);
            invalidate();
        }
        return changed;
    }

    /**
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean addModifiers(Collection<? extends Modifier> modifiers) {
        boolean changed = this.modifiers.addAll(modifiers);
        if(changed) {
            modifiers.forEach(modifier -> modifier.addDependency(this));
            invalidate();
        }
        return changed;
    }

    /**
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        boolean changed = modifiers.remove(modifier);
        if(changed) {
            modifier.removeDependency(this);
            invalidate();
        }
        return changed;
    }

    /**
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this value changed as a result of this operation
     */
    @Override
    public boolean removeModifiers(Collection<? extends Modifier> modifiers) {
        boolean changed = this.modifiers.removeAll(modifiers);
        if(changed) {
            modifiers.forEach(modifier -> modifier.removeDependency(this));
            invalidate();
        }
        return changed;
    }

    /**
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this value
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    /**
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this value
     */
    @Override
    public boolean containsModifiers(Collection<? extends Modifier> modifiers) {
        return this.modifiers.containsAll(modifiers);
    }

    @Override
    protected void onInvalidate() {
        invalidated = true;
    }

    /**
     * @return {@link String} representation of this value in format <i>Value(base,value)</i>
     */
    @Override
    public String toString() {
        return String.format("Value(%s,%s)", getBase(), getValue());
    }

    private void calculate() {
        Numeral[] finalValue = {base};
        modifiers.forEach(modifier -> finalValue[0] = modifier.apply(finalValue[0]));
        value = finalValue[0];
        invalidated = false;
    }

    private void contextOperation(Runnable action, MathContext context) {
        Numeral old = base;

        if(base.getType().isInteger() && context.convertToDecimal()) {
            toDecimal();
        }

        action.run();

        if(context.convertResultTo() != null) {
            if(context.ignoreBadConversion()) {
                convertIfAllowed(context.convertResultTo());
            } else {
                try {
                    convert(context.convertResultTo());
                } catch(ExtendedArithmeticException e) {
                    base = old;
                    throw new ExtendedArithmeticException(e);
                }
            }
        }

        if(!base.equals(old)) {
            invalidate();
        }
    }
}
