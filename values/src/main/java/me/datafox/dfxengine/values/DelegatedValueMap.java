package me.datafox.dfxengine.values;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.handles.collection.HashHandleMap;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.comparison.ComparisonContext;
import me.datafox.dfxengine.values.api.comparison.MapComparisonContext;
import me.datafox.dfxengine.values.api.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class DelegatedValueMap implements ValueMap {
    private final Logger logger;
    private final HandleMap<Value> map;
    private final SortedSet<Modifier> modifiers;

    private NumeralMap baseNumeralMap;
    private NumeralMap valueNumeralMap;

    public DelegatedValueMap(HandleMap<Value> map) {
        logger = LoggerFactory.getLogger(getClass());
        this.map = map;
        modifiers = new TreeSet<>();
    }

    @Override
    public void convert(NumeralType type) throws ArithmeticException {
        if(stream().allMatch(val -> val.canConvert(type))) {
            values().forEach(val -> val.convert(type));
        } else {
            throw new ArithmeticException("number overflow");
        }
    }

    @Override
    public void convert(Collection<Handle> handles, NumeralType type) throws ArithmeticException {
        if(getExisting(handles).allMatch(val -> val.canConvert(type))) {
            getExisting(handles).forEach(val -> val.convert(type));
        } else {
            throw new ArithmeticException("number overflow");
        }
    }

    @Override
    public void convert(Map<Handle,NumeralType> types) throws ArithmeticException {
        if(getExisting(types.keySet()).allMatch(val -> val.canConvert(types.get(val.getHandle())))) {
            getExisting(types.keySet()).forEach(val -> val.convert(types.get(val.getHandle())));
        } else {
            throw new ArithmeticException("number overflow");
        }
    }

    @Override
    public void convertAllowed(NumeralType type) {
        values().forEach(val -> val.convertIfAllowed(type));
    }

    @Override
    public void toInteger() {
        values().forEach(Value::toInteger);
    }

    @Override
    public void toDecimal() {
        values().forEach(Value::toDecimal);
    }

    @Override
    public void toSmallestType() {
        values().forEach(Value::toSmallestType);
    }

    @Override
    public void toSmallestType(Collection<Handle> handles) {
        getExisting(handles).forEach(Value::toSmallestType);
    }

    @Override
    public void set(Numeral value) {
        values().forEach(val -> val.set(value));
    }

    @Override
    public void set(MapMathContext context, Collection<Handle> handles, Numeral value) {
        contextOperation(val -> val.set(value), handles, context);
    }

    @Override
    public void set(MapMathContext context, Map<Handle,Numeral> values) {
        contextOperation((num, val) -> val.set(num), values, context);
    }

    @Override
    public void apply(SourceOperation operation, MathContext context) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context)), context);
    }

    @Override
    public void apply(SourceOperation operation, MapMathContext context, Collection<Handle> handles) {
        contextOperation(val -> val.apply(operation, context), handles, context);
    }

    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter)), context);
    }

    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context,
                      Collection<Handle> handles, Numeral parameter) {
        contextOperation(val -> val.apply(operation, context, parameter), handles, context);
    }

    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context, Map<Handle,Numeral> parameters) {
        contextOperation((num, val) -> val.apply(operation, context, num), parameters, context);
    }

    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter1, parameter2)), context);
    }

    @Override
    public void apply(DualParameterOperation operation, MapMathContext context,
                      Collection<Handle> handles, Numeral parameter1, Numeral parameter2) {
        contextOperation(val -> val.apply(operation, context, parameter1, parameter2), handles, context);
    }

    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameters)), context);
    }

    @Override
    public void apply(Operation operation, MapMathContext context, Collection<Handle> handles, Numeral ... parameters) {
        contextOperation(val -> val.apply(operation, context, parameters), handles, context);
    }

    @Override
    public void apply(Operation operation, MapMathContext context, Map<Handle,Numeral[]> parameters) {
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn("exception may cause operation to fail midway");
        }

        if(context.createNonExistingAs() != null) {
            createNonExisting(parameters.keySet(), context.createNonExistingAs());
            values().forEach(val -> val.apply(operation, context, parameters.get(val.getHandle())));
        } else {
            getExisting(parameters.keySet())
                    .forEach(val -> val.apply(operation, context, parameters.get(val.getHandle())));
        }
    }

    @Override
    public boolean compare(Comparison comparison, ComparisonContext context, Numeral other) {
        return stream().allMatch(comparison.predicate(context, other));
    }

    @Override
    public boolean compare(Comparison comparison, MapComparisonContext context,
                           Collection<Handle> handles, Numeral other) {
        return contextComparison((val, num) -> val.compare(comparison, context, num),
                comparison::compare, handles, other, context);
    }

    @Override
    public boolean compare(Comparison comparison, MapComparisonContext context, Map<Handle,Numeral> others) {
        return contextComparison((val, num) -> val.compare(comparison, context, num),
                comparison::compare, others, context);
    }

    @Override
    public Map<Handle,Numeral> getBaseNumeralMap() {
        if(baseNumeralMap == null) {
            baseNumeralMap = new NumeralMap(false);
        }
        return baseNumeralMap;
    }

    @Override
    public Map<Handle,Numeral> getValueNumeralMap() {
        if(valueNumeralMap == null) {
            valueNumeralMap = new NumeralMap(true);
        }
        return valueNumeralMap;
    }

    @Override
    public Collection<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    @Override
    public boolean addModifier(Modifier modifier) {
        boolean changed = modifiers.add(modifier);
        if(changed) {
            values().forEach(val -> val.addModifier(modifier));
        }
        return changed;
    }

    @Override
    public boolean addModifiers(Collection<Modifier> modifiers) {
        boolean changed = this.modifiers.addAll(modifiers);
        if(changed) {
            values().forEach(val -> val.addModifiers(modifiers));
        }
        return changed;
    }

    @Override
    public boolean removeModifier(Modifier modifier) {
        boolean changed = modifiers.remove(modifier);
        if(changed) {
            values().forEach(val -> val.removeModifier(modifier));
        }
        return changed;
    }

    @Override
    public boolean removeModifiers(Collection<Modifier> modifiers) {
        boolean changed = this.modifiers.removeAll(modifiers);
        if(changed) {
            values().forEach(val -> val.removeModifiers(modifiers));
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
    public Space getSpace() {
        return map.getSpace();
    }

    @Override
    public Value putHandled(Value value) {
        value.addModifiers(modifiers);
        Value old = map.putHandled(value);
        removeModifiersFrom(old);
        return old;
    }

    @Override
    public boolean containsById(String id) {
        return map.containsById(id);
    }

    @Override
    public boolean containsAll(Collection<Handle> handles) {
        return map.containsAll(handles);
    }

    @Override
    public boolean containsAllById(Collection<String> ids) {
        return map.containsAllById(ids);
    }

    @Override
    public Value getById(String id) {
        return map.getById(id);
    }

    @Override
    public Value removeById(String id) {
        Value old = map.removeById(id);
        removeModifiersFrom(old);
        return old;
    }

    @Override
    public boolean removeAll(Collection<Handle> handles) {
        boolean changed = false;
        for(Handle handle : handles) {
            if(remove(handle) != null) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAllById(Collection<String> ids) {
        boolean changed = false;
        for(String id : ids) {
            if(removeById(id) != null) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Value get(Object key) {
        return map.get(key);
    }

    @Override
    public Value put(Handle key, Value value) {
        value.addModifiers(modifiers);
        Value old = map.put(key, value);
        removeModifiersFrom(old);
        return old;
    }

    @Override
    public Value remove(Object key) {
        Value old = map.remove(key);
        removeModifiersFrom(old);
        return old;
    }

    @Override
    public void putAll(Map<? extends Handle,? extends Value> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        values().forEach(this::removeModifiersFrom);
        map.clear();
    }

    @Override
    public Set<Handle> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Value> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Handle,Value>> entrySet() {
        return map.entrySet();
    }

    private Stream<Value> getExisting(Collection<Handle> handles) {
        return handles.stream().filter(this::containsKey).map(this::get);
    }

    private Stream<Handle> getNonExisting(Collection<Handle> handles) {
        return handles.stream()
                .filter(Predicate.not(this::containsKey))
                .filter(handle -> getSpace().equals(handle.getSpace()));
    }

    private void createNonExisting(Collection<Handle> handles, Numeral initialValue) {
        getNonExisting(handles).forEach(handle -> putHandled(new ValueImpl(handle, initialValue)));
    }

    private void contextOperation(Runnable operation, MathContext context) {
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn("exception may cause operation to fail midway");
        }
        operation.run();
    }

    private void contextOperation(Consumer<Value> operation, Collection<Handle> handles, MapMathContext context) {
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn("exception may cause operation to fail midway");
        }

        if(context.createNonExistingAs() != null) {
            createNonExisting(handles, context.createNonExistingAs());
            values().forEach(operation);
        } else {
            getExisting(handles).forEach(operation);
        }
    }

    private void contextOperation(BiConsumer<Numeral, Value> operation, Map<Handle, Numeral> values, MapMathContext context) {
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn("exception may cause operation to fail midway");
        }

        if(context.createNonExistingAs() != null) {
            createNonExisting(values.keySet(), context.createNonExistingAs());
            values().forEach(val -> operation.accept(values.get(val.getHandle()), val));
        } else {
            getExisting(values.keySet())
                    .forEach(val -> operation.accept(values.get(val.getHandle()), val));
        }
    }

    private boolean contextComparison(BiFunction<Value,Numeral,Boolean> valueComparison, BiFunction<Numeral,Numeral,Boolean> numeralComparison, Collection<Handle> handles, Numeral value, MapComparisonContext context) {
        boolean existing = getExisting(handles).allMatch(val -> valueComparison.apply(val, value));

        if(context.getTreatNonExistingAs() == null) {
            return existing;
        }

        return existing && numeralComparison.apply(context.getTreatNonExistingAs(), value);
    }

    private boolean contextComparison(BiFunction<Value,Numeral,Boolean> valueComparison, BiFunction<Numeral,Numeral,Boolean> numeralComparison, Map<Handle,Numeral> values, MapComparisonContext context) {
        boolean existing = getExisting(values.keySet()).allMatch(val -> valueComparison.apply(val, values.get(val.getHandle())));

        if(context.getTreatNonExistingAs() == null) {
            return existing;
        }

        return existing && getNonExisting(values.keySet()).allMatch(handle ->
                numeralComparison.apply(context.getTreatNonExistingAs(), values.get(handle)));
    }

    private void removeModifiersFrom(Value old) {
        if(old == null || containsValue(old)) return;
        old.removeModifiers(modifiers);
    }

    public class NumeralMap implements Map<Handle,Numeral> {
        private final boolean modified;

        NumeralMap(boolean modified) {
            this.modified = modified;
        }

        @Override
        public int size() {
            return DelegatedValueMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return DelegatedValueMap.this.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return DelegatedValueMap.this.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return DelegatedValueMap.this.stream().map(this::getInternal).anyMatch(Predicate.isEqual(value));
        }

        @Override
        public Numeral get(Object key) {
            return getInternal(DelegatedValueMap.this.get(key));
        }

        @Override
        public Numeral put(Handle key, Numeral value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Numeral remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends Handle,? extends Numeral> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Handle> keySet() {
            return DelegatedValueMap.this.keySet();
        }

        @Override
        public Collection<Numeral> values() {
            return DelegatedValueMap.this.stream().map(this::getInternal).collect(Collectors.toList());
        }

        @Override
        public Set<Entry<Handle,Numeral>> entrySet() {
            return DelegatedValueMap.this.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), getInternal(entry.getValue()))).collect(Collectors.toSet());
        }

        private Numeral getInternal(Value value) {
            if(value == null) {
                return null;
            }

            if(modified) {
                return value.getValue();
            }

            return value.getBase();
        }
    }

    public static Builder builder(Space space) {
        return new Builder(space);
    }

    public static class Builder {
        private final Space space;
        private Function<Space, HandleMap<Value>> map;
        private final Set<Value> values;
        private final Set<Modifier> modifiers;

        private Builder(Space space) {
            this.space = space;
            map = HashHandleMap::new;
            values = new HashSet<>();
            modifiers = new HashSet<>();
        }

        public Builder map(Function<Space, HandleMap<Value>> map) {
            this.map = map;
            return this;
        }

        public Builder value(Value value) {
            values.add(value);
            return this;
        }

        public Builder values(Collection<Value> values) {
            this.values.addAll(values);
            return this;
        }

        public Builder clearValues() {
            values.clear();
            return this;
        }

        public Builder modifier(Modifier modifier) {
            modifiers.add(modifier);
            return this;
        }

        public Builder modifiers(Collection<Modifier> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        public Builder clearModifiers() {
            modifiers.clear();
            return this;
        }

        public ValueMap build() {
            ValueMap map = new DelegatedValueMap(this.map.apply(space));
            values.forEach(map::putHandled);
            map.addModifiers(modifiers);
            return map;
        }
    }
}
