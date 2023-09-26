package me.datafox.dfxengine.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
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
 * @author datafox
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class ValueImpl extends DependencyDependent implements Value {
    private final Handle handle;

    private final SortedSet<Modifier> modifiers;

    private Numeral base;
    private Numeral value;

    private boolean invalidated;

    public ValueImpl(Handle handle, Numeral value) {
        super(LoggerFactory.getLogger(ValueImpl.class));
        this.handle = handle;
        modifiers = new TreeSet<>();
        base = value;
        this.value = value;
        invalidated = false;
    }

    @Override
    public Numeral getValue() {
        if(invalidated) {
            calculate();
        }

        return value;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean convert(NumeralType type) throws ArithmeticException {
        if(base.getType().equals(type)) {
            return false;
        }
        base = base.convert(type);
        invalidate();
        return true;
    }

    @Override
    public boolean convertIfAllowed(NumeralType type) {
        Numeral old = base;
        base = base.convertIfAllowed(type);
        boolean changed = !old.equals(base);
        if(changed) {
            invalidate();
        }
        return changed;
    }

    @Override
    public boolean convertToDecimal() {
        if(base.getType().isDecimal()) {
            return false;
        }
        base = base.convertToDecimal();
        invalidate();
        return true;
    }

    @Override
    public boolean canConvert(NumeralType type) {
        return base.canConvert(type);
    }

    @Override
    public void toSmallestType() {
        base = base.toSmallestType();
        invalidate();
    }

    @Override
    public void set(Numeral value, MathContext context) {
        contextOperation(() -> base = value, context);
    }

    @Override
    public void apply(SourceOperation operation, MathContext context) {
        contextOperation(() -> base = operation.apply(base), context);
    }

    @Override
    public void apply(SingleParameterOperation operation, Numeral parameter, MathContext context) {
        contextOperation(() -> base = operation.apply(base, parameter), context);
    }

    @Override
    public void apply(DualParameterOperation operation, Numeral parameter1, Numeral parameter2, MathContext context) {
        contextOperation(() -> base = operation.apply(base, parameter1, parameter2), context);
    }

    @Override
    public void apply(Operation operation, List<Numeral> parameters, MathContext context) {
        contextOperation(() -> base = operation.apply(base, parameters), context);
    }

    @Override
    public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
        return comparison.compare(context.useModifiedValue() ? getValue() : base, other);
    }

    @Override
    public Collection<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    @Override
    public boolean addModifier(Modifier modifier) {
        boolean changed = modifiers.add(modifier);
        if(changed) {
            modifier.addDependency(this);
            invalidate();
        }
        return changed;
    }

    @Override
    public boolean addModifiers(Collection<Modifier> modifiers) {
        boolean changed = this.modifiers.addAll(modifiers);
        if(changed) {
            modifiers.forEach(modifier -> modifier.addDependency(this));
            invalidate();
        }
        return changed;
    }

    @Override
    public boolean removeModifier(Modifier modifier) {
        boolean changed = modifiers.remove(modifier);
        if(changed) {
            modifier.removeDependency(this);
            invalidate();
        }
        return changed;
    }

    @Override
    public boolean removeModifiers(Collection<Modifier> modifiers) {
        boolean changed = this.modifiers.removeAll(modifiers);
        if(changed) {
            modifiers.forEach(modifier -> modifier.removeDependency(this));
            invalidate();
        }
        return changed;
    }

    @Override
    public boolean containsModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    @Override
    public boolean containsModifiers(Collection<Modifier> modifiers) {
        return this.modifiers.containsAll(modifiers);
    }

    @Override
    protected void onInvalidate() {
        invalidated = true;
    }

    private void calculate() {
        Numeral[] finalValue = {value};
        modifiers.forEach(modifier -> finalValue[0] = modifier.apply(finalValue[0]));
        value = finalValue[0];
        invalidated = false;
    }

    private void contextOperation(Runnable action, MathContext context) {
        if(base.getType().isInteger() && context.convertToDecimal()) {
            convertToDecimal();
        }

        action.run();

        if(context.conversionResult() != null) {
            if(context.ignoreBadConversion()) {
                convertIfAllowed(context.conversionResult());
            } else {
                convert(context.conversionResult());
            }
        }

        invalidate();
    }

    public static ValueImpl of(Handle handle, int i) {
        return new ValueImpl(handle, Numerals.valueOf(i));
    }

    public static ValueImpl of(Handle handle, long l) {
        return new ValueImpl(handle, Numerals.valueOf(l));
    }

    public static ValueImpl of(Handle handle, BigInteger bi) {
        return new ValueImpl(handle, Numerals.valueOf(bi));
    }

    public static ValueImpl of(Handle handle, float f) {
        return new ValueImpl(handle, Numerals.valueOf(f));
    }

    public static ValueImpl of(Handle handle, double d) {
        return new ValueImpl(handle, Numerals.valueOf(d));
    }

    public static ValueImpl of(Handle handle, BigDecimal bd) {
        return new ValueImpl(handle, Numerals.valueOf(bd));
    }

    public static ValueImpl of(Handle handle, String val) {
        return new ValueImpl(handle, Numerals.valueOf(val));
    }
}
