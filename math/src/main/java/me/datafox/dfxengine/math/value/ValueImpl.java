package me.datafox.dfxengine.math.value;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.*;
import me.datafox.dfxengine.math.api.comparison.Comparison;
import me.datafox.dfxengine.math.api.comparison.ComparisonContext;
import me.datafox.dfxengine.math.api.modifier.Modifier;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.api.operation.Operation;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.api.operation.SourceOperation;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author datafox
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class ValueImpl extends DependencyDependent implements Value {
    private final Handle handle;

    @Getter(AccessLevel.NONE)
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
    public void convert(NumeralType type) throws ArithmeticException {
        if(base.getType().equals(type)) {
            return;
        }
        base = base.convert(type);
        invalidate();
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
        Numeral old = base;
        base = base.convertToDecimal();
        boolean changed = !old.equals(base);
        if(changed) {
            invalidate();
        }
        return changed;
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
    public void apply(Operation operation, List<Numeral> parameters, MathContext context) {
        contextOperation(() -> base = operation.apply(base, parameters), context);
    }

    @Override
    public boolean compare(Comparison comparison, Numeral other, ComparisonContext context) {
        return comparison.compare(context.isUseModifiedValue() ? getValue() : base, other);
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
        Numeral[] finalValue = { value };
        modifiers.forEach(modifier -> finalValue[0] = modifier.apply(finalValue[0]));
        value = finalValue[0];
        invalidated = false;
    }

    private void contextOperation(Runnable action, MathContext context) {
        if(base.getType().isInteger() && context.isConvertToDecimal()) {
            convertToDecimal();
        }
        action.run();
        context.getConvertResult()
                .filter(Predicate.not(Predicate.isEqual(base.getType())))
                .ifPresent(context.isIgnoreBadConversion() ? this::convertIfAllowed : this::convert);
        invalidate();
    }
}
