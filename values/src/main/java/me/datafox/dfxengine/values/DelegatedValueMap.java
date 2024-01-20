package me.datafox.dfxengine.values;

import me.datafox.dfxengine.collections.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.utils.LogUtils;
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
 * An implementation of {@link ValueMap} that can be backed with any implementation of {@link HandleMap}. Includes a
 * {@link Builder} for easy instantiation.
 *
 * @author datafox
 */
public class DelegatedValueMap implements ValueMap {
    private final Logger logger;
    private final HandleMap<Value> map;
    private final Set<Modifier> modifiers;

    private NumeralMap baseNumeralMap;
    private NumeralMap valueNumeralMap;

    /**
     * @param map {@link HandleMap} to back this map with
     */
    public DelegatedValueMap(HandleMap<Value> map) {
        logger = LoggerFactory.getLogger(getClass());
        this.map = map;
        modifiers = new HashSet<>();
    }

    /**
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the {@link Value Values} in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public void convert(NumeralType type) {
        if(stream().allMatch(val -> val.canConvert(type))) {
            values().forEach(val -> val.convert(type));
        } else {
            throw LogUtils.logExceptionAndGet(logger, "number overflow",
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the {@link Value Values} to be converted
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public void convert(Collection<? extends Handle> handles, NumeralType type) {
        if(getExisting(handles).allMatch(val -> val.canConvert(type))) {
            getExisting(handles).forEach(val -> val.convert(type));
        } else {
            throw LogUtils.logExceptionAndGet(logger, "number overflow",
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param types map of {@link Handle Handles} and {@link NumeralType NumeralTypes} for the {@link Value Values}
     * represented by those Handles to be converted to.
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public void convert(Map<? extends Handle,NumeralType> types) {
        if(getExisting(types.keySet()).allMatch(val -> val.canConvert(types.get(val.getHandle())))) {
            getExisting(types.keySet()).forEach(val -> val.convert(types.get(val.getHandle())));
        } else {
            throw LogUtils.logExceptionAndGet(logger, "number overflow",
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * Only converts {@link Value Values} of this map that can be converted to the specified type. In other words, calls
     * {@link Numeral#convertIfAllowed(NumeralType)} on every Value of this map.
     *
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public void convertAllowed(NumeralType type) {
        values().forEach(val -> val.convertIfAllowed(type));
    }

    /**
     * Converts all {@link Value Values} to the smallest integer type that can hold its represented value. Values that
     * are already integers are not converted.
     */
    @Override
    public void toInteger() {
        values().forEach(Value::toInteger);
    }

    /**
     * Converts all {@link Value Values} to the smallest decimal type that can hold its represented value. Values that
     * are already decimals are not converted.
     */
    @Override
    public void toDecimal() {
        values().forEach(Value::toDecimal);
    }

    /**
     * Converts all {@link Value Values} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations.
     */
    @Override
    public void toSmallestType() {
        values().forEach(Value::toSmallestType);
    }

    /**
     * Converts specified {@link Value Values} to the smallest type that can hold its represented value. Will not
     * convert between integer and decimal representations. {@link Handle Handles} that are not a part of this map's
     * associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the values to be converted
     */
    @Override
    public void toSmallestType(Collection<? extends Handle> handles) {
        getExisting(handles).forEach(Value::toSmallestType);
    }

    /**
     * @param value {@link Numeral} for all {@link Value Values} of this map to be set to
     */
    @Override
    public void set(Numeral value) {
        values().forEach(val -> val.set(value));
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     */
    @Override
    public void set(MapMathContext context, Collection<? extends Handle> handles, Numeral value) {
        contextOperation(val -> val.set(value), handles, context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     */
    @Override
    public void set(MapMathContext context, Map<? extends Handle,Numeral> values) {
        contextOperation((num, val) -> val.set(num), values, context);
    }

    /**
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context)), context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     */
    @Override
    public void apply(SourceOperation operation, MapMathContext context, Collection<? extends Handle> handles) {
        contextOperation(val -> val.apply(operation, context), handles, context);
    }

    /**
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter)), context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter parameter for the operation
     */
    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context,
                      Collection<? extends Handle> handles, Numeral parameter) {
        contextOperation(val -> val.apply(operation, context, parameter), handles, context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     */
    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context, Map<? extends Handle,Numeral> parameters) {
        contextOperation((num, val) -> val.apply(operation, context, num), parameters, context);
    }

    /**
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter1, parameter2)), context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link DualParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     */
    @Override
    public void apply(DualParameterOperation operation, MapMathContext context,
                      Collection<? extends Handle> handles, Numeral parameter1, Numeral parameter2) {
        contextOperation(val -> val.apply(operation, context, parameter1, parameter2), handles, context);
    }

    /**
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameters)), context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    @Override
    public void apply(Operation operation, MapMathContext context, Collection<? extends Handle> handles, Numeral ... parameters) {
        contextOperation(val -> val.apply(operation, context, parameters), handles, context);
    }

    /**
     * {@link Handle Handles} that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     */
    @Override
    public void apply(Operation operation, MapMathContext context, Map<? extends Handle,Numeral[]> parameters) {
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

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    @Override
    public boolean compare(Comparison comparison, ComparisonContext context, Numeral other) {
        return stream().allMatch(comparison.predicate(context, other));
    }

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link MapComparisonContext} for the comparison
     * @param handles {@link Handle Handles} of the {@link Value Values} to be compared
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    @Override
    public boolean compare(Comparison comparison, MapComparisonContext context,
                           Collection<? extends Handle> handles, Numeral other) {
        return contextComparison((val, num) -> val.compare(comparison, context, num),
                comparison::compare, handles, other, context);
    }

    /**
     * @param comparison {@link Comparison} to be used
     * @param context {@link MapComparisonContext} for the comparison
     * @param others map of {@link Handle Handles} of the {@link Value Values} to be compared and
     * {@link Numeral Numerals} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     * represented by the specified keys
     */
    @Override
    public boolean compare(Comparison comparison, MapComparisonContext context, Map<? extends Handle,Numeral> others) {
        return contextComparison((val, num) -> val.compare(comparison, context, num),
                comparison::compare, others, context);
    }

    /**
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values. The returned map does not have any state and instead provides a view to this map
     */
    @Override
    public Map<Handle,Numeral> getBaseNumeralMap() {
        if(baseNumeralMap == null) {
            baseNumeralMap = new NumeralMap(false);
        }
        return baseNumeralMap;
    }

    /**
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()} as
     * the map values. The returned map does not have any state and instead provides a view to this map
     */
    @Override
    public Map<Handle,Numeral> getValueNumeralMap() {
        if(valueNumeralMap == null) {
            valueNumeralMap = new NumeralMap(true);
        }
        return valueNumeralMap;
    }

    /**
     * @return {@link Modifier Modifiers} associated with this map
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    /**
     * {@link Modifier Modifiers} added to this map will be added to all {@link Value Values} of this map, including
     * ones that are added to this map after this operation. To add a modifier to a single value, use
     * {@link Value#addModifier(Modifier)} instead.
     *
     * @param modifier {@link Modifier} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    @Override
    public boolean addModifier(Modifier modifier) {
        boolean changed = modifiers.add(modifier);
        if(changed) {
            values().forEach(val -> val.addModifier(modifier));
        }
        return changed;
    }

    /**
     * {@link Modifier Modifiers} added to this map will be added to all {@link Value Values} of this map, including
     * ones that are added to this map after this operation. To add modifiers to a single value, use
     * {@link Value#addModifiers(Collection)}} instead.
     *
     * @param modifiers {@link Modifier Modifiers} to be added
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    @Override
    public boolean addModifiers(Collection<? extends Modifier> modifiers) {
        boolean changed = this.modifiers.addAll(modifiers);
        if(changed) {
            values().forEach(val -> val.addModifiers(modifiers));
        }
        return changed;
    }

    /**
     * @param modifier {@link Modifier} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    @Override
    public boolean removeModifier(Modifier modifier) {
        boolean changed = modifiers.remove(modifier);
        if(changed) {
            values().forEach(val -> val.removeModifier(modifier));
        }
        return changed;
    }

    /**
     * @param modifiers {@link Modifier Modifiers} to be removed
     * @return {@code true} if the {@link Modifier Modifiers} of this map changed as a result of this operation
     */
    @Override
    public boolean removeModifiers(Collection<? extends Modifier> modifiers) {
        boolean changed = this.modifiers.removeAll(modifiers);
        if(changed) {
            values().forEach(val -> val.removeModifiers(modifiers));
        }
        return changed;
    }

    /**
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this map
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    /**
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this map
     */
    @Override
    public boolean containsModifiers(Collection<? extends Modifier> modifiers) {
        return this.modifiers.containsAll(modifiers);
    }

    /**
     * @return {@link Space} associated with this map
     */
    @Override
    public Space getSpace() {
        return map.getSpace();
    }

    /**
     * Associates a {@link Value} in this map. If the map previously contained a mapping for the {@link Handle}, the old
     * Value is replaced.
     *
     * @param value {@link Value} to be associated in this map with its associated {@link Handle} as a key
     * @return the previously associated {@link Value} in this map, or {@code null} if there was no previous association
     *
     * @throws IllegalArgumentException if the associated {@link Handle} is not contained in the {@link Space}
     * associated with this map
     */
    @Override
    public Value putHandled(Value value) {
        Value old = map.putHandled(value);
        value.addModifiers(modifiers);
        removeModifiersFrom(old);
        return old;
    }

    /**
     * @param id id of the {@link Value} to be checked for
     * @return {@code true} if this map contains a {@link Value} with the specified id
     */
    @Override
    public boolean containsById(String id) {
        return map.containsById(id);
    }

    /**
     * @param handles {@link Value Values} to be checked for
     * @return {@code true} if this map contains all the specified {@link Value Values}
     */
    @Override
    public boolean containsAll(Collection<? extends Handle> handles) {
        return map.containsAll(handles);
    }

    /**
     * @param ids ids of the {@link Value Values} to be checked for
     * @return {@code true} if this map contains all {@link Value Values} with the specified ids
     */
    @Override
    public boolean containsAllById(Collection<String> ids) {
        return map.containsAllById(ids);
    }

    /**
     * @param id id of the requested {@link Value}
     * @return {@link Value} with the specified id, or {@code null} if none are present
     */
    @Override
    public Value getById(String id) {
        return map.getById(id);
    }

    /**
     * @param id id of the {@link Value} to be removed
     * @return {@link Value} with the specified id, or {@code null} if none were present
     */
    @Override
    public Value removeById(String id) {
        Value old = map.removeById(id);
        removeModifiersFrom(old);
        return old;
    }

    /**
     * @param handles {@link Handle Handles} of the {@link Value Values }to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
    @Override
    public boolean removeAll(Collection<? extends Handle> handles) {
        boolean changed = false;
        for(Handle handle : handles) {
            if(remove(handle) != null) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * @param ids ids of the {@link Value Values} to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
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

    /**
     * @return number of {@link Value Values} in this map
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * @return {@code true} if this map contains no {@link Value Values}
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     */
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * @param key the key whose associated value is to be returned
     * @return {@link Value} to which the specified key is mapped, or {@code null} if this map contains no mapping for
     * the key
     */
    @Override
    public Value get(Object key) {
        return map.get(key);
    }

    /**
     * This method ignores the key parameter, it is recommended to use {@link #putHandled(Value)} instead.
     *
     * @param key ignored parameter
     * @param value {@link Value} to be associated with this map
     * @return previously associated {@link Value} in this map, or {@code null} if there was no previous association
     */
    @Override
    public Value put(Handle key, Value value) {
        logger.warn("The specified Handle is ignored and the Value's Handle is used as a key instead, consider using ValueMap.putHandled(Value) instead");
        return putHandled(value);
    }

    /**
     * @param key key whose mapping is to be removed from the map
     * @return previous {@link Value} associated with the key, or {@code null} if there was no mapping
     */
    @Override
    public Value remove(Object key) {
        Value old = map.remove(key);
        removeModifiersFrom(old);
        return old;
    }

    /**
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends Handle, ? extends Value> m) {
        m.forEach(this::put);
    }

    /**
     * Removes all of the {@link Value Values} from this map.
     */
    @Override
    public void clear() {
        values().forEach(this::removeModifiersFrom);
        map.clear();
    }

    /**
     * @return {@link Handle Handles} of the {@link Value Values} associated with this map
     */
    @Override
    public Set<Handle> keySet() {
        return map.keySet();
    }

    /**
     * @return {@link Value Values} associated with this map
     */
    @Override
    public Collection<Value> values() {
        return map.values();
    }

    /**
     * @return {@link Handle Handles} and {@link Value Values} associated with this map
     */
    @Override
    public Set<Entry<Handle,Value>> entrySet() {
        return map.entrySet();
    }

    private Stream<Value> getExisting(Collection<? extends Handle> handles) {
        return handles.stream().filter(this::containsKey).map(this::get);
    }

    private Stream<? extends Handle> getNonExisting(Collection<? extends Handle> handles) {
        if(handles.stream().noneMatch(Predicate.not(handle -> getSpace().equals(handle.getSpace())))) {
            logger.warn("Handles that are not part of " + getSpace() + " are ignored");
        }
        return handles.stream()
                .filter(Predicate.not(this::containsKey))
                .filter(handle -> getSpace().equals(handle.getSpace()));
    }

    private void createNonExisting(Collection<? extends Handle> handles, Numeral initialValue) {
        getNonExisting(handles).forEach(handle -> putHandled(new ValueImpl(handle, initialValue)));
    }

    private void contextOperation(Runnable operation, MathContext context) {
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn("exception may cause operation to fail midway");
        }
        operation.run();
    }

    private void contextOperation(Consumer<Value> operation, Collection<? extends Handle> handles, MapMathContext context) {
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

    private void contextOperation(BiConsumer<Numeral, Value> operation, Map<? extends Handle, Numeral> values, MapMathContext context) {
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

    private boolean contextComparison(BiFunction<Value,Numeral,Boolean> valueComparison,
                                      BiFunction<Numeral,Numeral,Boolean> numeralComparison,
                                      Collection<? extends Handle> handles, Numeral value, MapComparisonContext context) {
        boolean existing = getExisting(handles).allMatch(val -> valueComparison.apply(val, value));

        if(context.treatNonExistingAs() == null) {
            return existing;
        }

        return existing && numeralComparison.apply(context.treatNonExistingAs(), value);
    }

    private boolean contextComparison(BiFunction<Value,Numeral,Boolean> valueComparison,
                                      BiFunction<Numeral,Numeral,Boolean> numeralComparison,
                                      Map<? extends Handle,Numeral> values, MapComparisonContext context) {
        boolean existing = getExisting(values.keySet()).allMatch(val -> valueComparison.apply(val, values.get(val.getHandle())));

        if(context.treatNonExistingAs() == null) {
            return existing;
        }

        return existing && getNonExisting(values.keySet()).allMatch(handle ->
                numeralComparison.apply(context.treatNonExistingAs(), values.get(handle)));
    }

    private void removeModifiersFrom(Value old) {
        if(old == null || containsValue(old)) return;
        old.removeModifiers(modifiers);
    }

    /**
     * A view to a {@link ValueMap} with the {@link Numeral} representations of the {@link Value Values}.
     */
    public class NumeralMap implements Map<Handle,Numeral> {
        private final boolean modified;

        NumeralMap(boolean modified) {
            this.modified = modified;
        }

        /**
         * @return the number of {@link Numeral Numerals} in this map
         */
        @Override
        public int size() {
            return DelegatedValueMap.this.size();
        }

        /**
         * @return {@code true} if this map contains no {@link Numeral Numerals}
         */
        @Override
        public boolean isEmpty() {
            return DelegatedValueMap.this.isEmpty();
        }

        /**
         * @param key key whose presence in this map is to be tested
         * @return {@code true} if this map contains a mapping for the specified key
         */
        @Override
        public boolean containsKey(Object key) {
            return DelegatedValueMap.this.containsKey(key);
        }

        /**
         * @param value value whose presence in this map is to be tested
         * @return {@code true} if this map maps one or more keys to the specified value
         */
        @Override
        public boolean containsValue(Object value) {
            return DelegatedValueMap.this.stream().map(this::getInternal).anyMatch(Predicate.isEqual(value));
        }

        /**
         * @param key the key whose associated value is to be returned
         * @return {@link Numeral} to which the specified key is mapped, or {@code null} if this map contains no mapping
         * for the key
         */
        @Override
        public Numeral get(Object key) {
            return getInternal(DelegatedValueMap.this.get(key));
        }

        /**
         * This method will always throw {@link UnsupportedOperationException}.
         *
         * @param key ignored parameter
         * @param value ignored parameter
         * @return this method will never return
         */
        @Override
        public Numeral put(Handle key, Numeral value) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method will always throw {@link UnsupportedOperationException}.
         *
         * @param key ignored parameter
         * @return this method will never return
         */
        @Override
        public Numeral remove(Object key) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method will always throw {@link UnsupportedOperationException}.
         *
         * @param m ignored parameter
         */
        @Override
        public void putAll(Map<? extends Handle,? extends Numeral> m) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method will always throw {@link UnsupportedOperationException}.
         */
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return {@link Handle Handles} of the {@link Numeral Numerals} associated with this map
         */
        @Override
        public Set<Handle> keySet() {
            return DelegatedValueMap.this.keySet();
        }

        /**
         * @return {@link Numeral Numerals} associated with this map
         */
        @Override
        public Collection<Numeral> values() {
            return DelegatedValueMap.this.stream().map(this::getInternal).collect(Collectors.toList());
        }

        /**
         * @return {@link Handle Handles} and {@link Numeral Numerals} associated with this map
         */
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

    /**
     * @param space {@link Space} to be associated with the {@link Builder}
     * @return {@link Builder} with the specified {@link Space}
     */
    public static Builder builder(Space space) {
        return new Builder(space);
    }

    /**
     * A builder for {@link ValueMap}.
     */
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

        /**
         * @param map {@link Function} to initialize the backing {@link HandleMap}
         * @return this builder
         */
        public Builder map(Function<Space, HandleMap<Value>> map) {
            this.map = map;
            return this;
        }

        /**
         * @param value {@link Value} to be associated with the {@link ValueMap}
         * @return this builder
         */
        public Builder value(Value value) {
            values.add(value);
            return this;
        }

        /**
         * @param values {@link Value Values} to be associated with the {@link ValueMap}
         * @return this builder
         */
        public Builder values(Collection<? extends Value> values) {
            this.values.addAll(values);
            return this;
        }

        /**
         * Removes all of the {@link Value Values} from the {@link ValueMap}.
         *
         * @return this builder
         */
        public Builder clearValues() {
            values.clear();
            return this;
        }

        /**
         * @param modifier {@link Modifier} to be associated with the {@link ValueMap}
         * @return this builder
         */
        public Builder modifier(Modifier modifier) {
            modifiers.add(modifier);
            return this;
        }

        /**
         * @param modifiers {@link Modifier Modifiers} to be associated with the {@link ValueMap}
         * @return this builder
         */
        public Builder modifiers(Collection<? extends Modifier> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        /**
         * Removes all of the {@link Modifier Modifiers} from the {@link ValueMap}.
         *
         * @return this builder
         */
        public Builder clearModifiers() {
            modifiers.clear();
            return this;
        }

        /**
         * @return {@link ValueMap} initialized by this builder
         */
        public ValueMap build() {
            ValueMap map = new DelegatedValueMap(this.map.apply(space));
            values.forEach(map::putHandled);
            map.addModifiers(modifiers);
            return map;
        }
    }
}
