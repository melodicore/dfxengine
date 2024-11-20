package me.datafox.dfxengine.values;

import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
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

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.*;

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
    private final boolean immutable;

    private NumeralMap baseNumeralMap;
    private NumeralMap valueNumeralMap;

    /**
     * Public constructor for {@link DelegatedValueMap}.
     *
     * @param map {@link HandleMap} to back this map with
     * @param immutable {@code true} if this map should contain immutable {@link Value Values}
     * @param logger {@link Logger} for this map
     */
    public DelegatedValueMap(HandleMap<Value> map, boolean immutable, Logger logger) {
        this.logger = logger;
        this.map = map;
        modifiers = new HashSet<>();
        this.immutable = immutable;

        if(map.values().stream().anyMatch(Value::isStatic)) {
            throw LogUtils.logExceptionAndGet(logger,
                    STATIC_VALUE_IN_MAP,
                    IllegalArgumentException::new);
        }

        if(map.values().stream().anyMatch(v -> v.isImmutable() != immutable)) {
            throw LogUtils.logExceptionAndGet(logger,
                    immutableValueMismatch(immutable),
                    IllegalArgumentException::new);
        }
    }

    /**
     * Returns {@code true} if this map is for immutable {@link Value Values}.
     *
     * @return {@code true} if this map is for immutable {@link Value Values}
     */
    @Override
    public boolean isImmutable() {
        return immutable;
    }

    /**
     * Converts all {@link Value Values} in this map to the specified type.
     *
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the {@link Value Values} in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void convert(NumeralType type) {
        checkImmutable();
        if(values().stream().allMatch(val -> val.canConvert(type))) {
            values().forEach(val -> val.convert(type));
        } else {
            throw LogUtils.logExceptionAndGet(logger, OVERFLOW,
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified type.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param handles {@link Handle Handles} of the {@link Value Values} to be converted
     * @param type {@link NumeralType} for the {@link Value Values} to be converted to
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void convert(Collection<? extends Handle> handles, NumeralType type) {
        checkImmutable();
        if(getExisting(handles).allMatch(val -> val.canConvert(type))) {
            getExisting(handles).forEach(val -> val.convert(type));
        } else {
            throw LogUtils.logExceptionAndGet(logger, OVERFLOW,
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified types.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param types map of {@link Handle Handles} and {@link NumeralType NumeralTypes} for the {@link Value Values}
     * represented by those Handles to be converted to.
     *
     * @throws ExtendedArithmeticException if any of the specified values in this map cannot be converted to the
     * specified type
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void convert(Map<? extends Handle,NumeralType> types) {
        checkImmutable();
        if(getExisting(types.keySet()).allMatch(val -> val.canConvert(types.get(val.getHandle())))) {
            getExisting(types.keySet()).forEach(val -> val.convert(types.get(val.getHandle())));
        } else {
            throw LogUtils.logExceptionAndGet(logger, OVERFLOW,
                    ExtendedArithmeticException::new);
        }
    }

    /**
     * Converts all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified type.
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
        if(!immutable) {
            values().forEach(val -> val.convertIfAllowed(type));
        }
    }

    /**
     * Converts all {@link Value Values} to the smallest integer type that can hold its represented value. Values that
     * are already integers are not converted.
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void toInteger() {
        checkImmutable();
        values().forEach(Value::toInteger);
    }

    /**
     * Converts all {@link Value Values} to the smallest decimal type that can hold its represented value. Values that
     * are already decimals are not converted.
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void toDecimal() {
        checkImmutable();
        values().forEach(Value::toDecimal);
    }

    /**
     * Converts all {@link Value Values} to the smallest type that can hold its represented value. Will not convert
     * between integer and decimal representations.
     */
    @Override
    public void toSmallestType() {
        if(!immutable) {
            values().forEach(Value::toSmallestType);
        }
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
        if(!immutable) {
            getExisting(handles).forEach(Value::toSmallestType);
        }
    }

    /**
     * Sets all {@link Value Values} of this map to the specified value.
     *
     * @param value {@link Numeral} for all {@link Value Values} of this map to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void set(Numeral value) {
        checkImmutable();
        values().forEach(val -> val.set(value));
    }

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified value.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param handles {@link Handle Handles} of the values to be changed
     * @param value {@link Numeral} for specified {@link Value Values} of this map to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void set(MapMathContext context, Collection<? extends Handle> handles, Numeral value) {
        contextOperation(val -> val.set(value), handles, context);
    }

    /**
     * Sets all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified values.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param context {@link MapMathContext} for this operation
     * @param values map of {@link Handle Handles} to be changed and {@link Numeral Numerals} for the specified
     * {@link Value Values} to be set to
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void set(MapMathContext context, Map<? extends Handle,Numeral> values) {
        contextOperation((num, val) -> val.set(num), values, context);
    }

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link SourceOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(SourceOperation operation, MathContext context) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context)), context);
    }

    /**
     * Applies a {@link SourceOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SourceOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(SourceOperation operation, MapMathContext context, Collection<? extends Handle> handles) {
        contextOperation(val -> val.apply(operation, context), handles, context);
    }

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link SingleParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(SingleParameterOperation operation, MathContext context, Numeral parameter) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter)), context);
    }

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context,
                      Collection<? extends Handle> handles, Numeral parameter) {
        contextOperation(val -> val.apply(operation, context, parameter), handles, context);
    }

    /**
     * Applies a {@link SingleParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link SingleParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(SingleParameterOperation operation, MapMathContext context, Map<? extends Handle,Numeral> parameters) {
        contextOperation((num, val) -> val.apply(operation, context, num), parameters, context);
    }

    /**
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map.
     *
     * @param operation {@link DualParameterOperation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(DualParameterOperation operation, MathContext context, Numeral parameter1, Numeral parameter2) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameter1, parameter2)), context);
    }

    /**
     * Applies a {@link DualParameterOperation} to all {@link Value Values} of this map with the specified
     * {@link Handle Handles}. Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link DualParameterOperation} to be applied to specified {@link Value Values} of this map
     * @param context {@link MapMathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameter1 first parameter for the operation
     * @param parameter2 second parameter for the operation
     *
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(DualParameterOperation operation, MapMathContext context,
                      Collection<? extends Handle> handles, Numeral parameter1, Numeral parameter2) {
        contextOperation(val -> val.apply(operation, context, parameter1, parameter2), handles, context);
    }

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(Operation operation, MathContext context, Numeral ... parameters) {
        contextOperation(() -> values().forEach(val -> val.apply(operation, context, parameters)), context);
    }

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param handles {@link Handle Handles} of the {@link Value Values} to be modified
     * @param parameters parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(Operation operation, MapMathContext context, Collection<? extends Handle> handles, Numeral ... parameters) {
        contextOperation(val -> val.apply(operation, context, parameters), handles, context);
    }

    /**
     * Applies an {@link Operation} to all {@link Value Values} of this map with the specified {@link Handle Handles}.
     * Handles that are not a part of this map's associated {@link Space} are ignored.
     *
     * @param operation {@link Operation} to be applied to all {@link Value Values} of this map
     * @param context {@link MathContext} for the operation
     * @param parameters map of {@link Handle Handles} of the {@link Value Values} to be modified and
     * {@link Numeral Numerals} to be used as parameters for the operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to
     * {@link Operation#getParameterCount()}
     * @throws UnsupportedOperationException if this map is for immutable {@link Value Values}
     */
    @Override
    public void apply(Operation operation, MapMathContext context, Map<? extends Handle,Numeral[]> parameters) {
        checkImmutable();
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn(MIDWAY_EXCEPTION);
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
     * Compares all {@link Value Values} in this map to a {@link Numeral} using the specified {@link Comparison}.
     *
     * @param comparison {@link Comparison} to be used
     * @param context {@link ComparisonContext} for the comparison
     * @param other {@link Numeral} to be compared to
     * @return {@code true} if the {@link Comparison} returns {@code true} for all {@link Value Values} of this map
     */
    @Override
    public boolean compare(Comparison comparison, ComparisonContext context, Numeral other) {
        return values().stream().allMatch(comparison.predicate(context, other));
    }

    /**
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to a {@link Numeral}
     * using the specified {@link Comparison}.
     *
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
     * Compares all {@link Value Values} in this map with the specified {@link Handle Handles} to the specified
     * {@link Numeral Numerals} using the specified {@link Comparison}.
     *
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
     * Returns a {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values. The returned map does not have any state and instead provides a view to this map.
     *
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getBase()} as
     * the map values.
     */
    @Override
    public Map<Handle,Numeral> getBaseNumeralMap() {
        if(baseNumeralMap == null) {
            baseNumeralMap = new NumeralMap(false);
        }
        return baseNumeralMap;
    }

    /**
     * Returns a {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()}
     * as the map values. The returned map does not have any state and instead provides a view to this map.
     *
     * @return {@link Map} containing all entries of this map, but with the return value of {@link Value#getValue()} as
     * the map values.
     */
    @Override
    public Map<Handle,Numeral> getValueNumeralMap() {
        if(valueNumeralMap == null) {
            valueNumeralMap = new NumeralMap(true);
        }
        return valueNumeralMap;
    }

    /**
     * Returns the {@link Modifier Modifiers} associated with this map.
     *
     * @return {@link Modifier Modifiers} associated with this map
     */
    @Override
    public Collection<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    /**
     * Registers a {@link Modifier} to this map. Modifiers added to this map will be added to all {@link Value Values}
     * of this map, including ones that are added to this map after this operation. To add a modifier to a single value,
     * use {@link Value#addModifier(Modifier)} instead.
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
     * Registers {@link Modifier Modifiers} to this map. Modifiers added to this map will be added to all
     * {@link Value Values} of this map, including ones that are added to this map after this operation. To add
     * modifiers to a single value, use {@link Value#addModifiers(Collection)}} instead.
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
     * Removes a {@link Modifier} from this map.
     *
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
     * Removes {@link Modifier Modifiers} from this map.
     *
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
     * Checks if a {@link Modifier} is present in this map.
     *
     * @param modifier {@link Modifier} to be checked for
     * @return {@code true} if the specified {@link Modifier} is associated with this map
     */
    @Override
    public boolean containsModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    /**
     * Checks if {@link Modifier Modifiers} are present in this map.
     *
     * @param modifiers {@link Modifier Modifiers} to be checked for
     * @return {@code true} if all of the specified {@link Modifier Modifiers} are associated with this map
     */
    @Override
    public boolean containsModifiers(Collection<? extends Modifier> modifiers) {
        return this.modifiers.containsAll(modifiers);
    }

    /**
     * Returns the {@link Space} associated with this map. All {@link Handle} keys present in this map must be
     * associated with this space.
     *
     * @return {@link Space} associated with this map
     */
    @Override
    public Space getSpace() {
        return map.getSpace();
    }

    /**
     * Returns {@code true} if this map contains a mapping for all the specified keys. The keys may either be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param keys {@link Handle} keys or their {@link String} ids whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for all the specified keys
     * @throws ClassCastException if any of the keys are of an inappropriate type for this map
     * @throws NullPointerException if any of the keys is {@code null}
     */
    @Override
    public boolean containsKeys(Collection<?> keys) {
        return map.containsKeys(keys);
    }

    /**
     * Associates a {@link Handled} value with its associated {@link Handle}. If the map previously contained a mapping
     * for the key, the old value is replaced by the specified value. The {@link Handle} key must be present in this
     * map's associated {@link Space}.
     *
     * @param value {@link Handled} value to be associated with its associated {@link Handle} key
     * @return the previous value associated with the key, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws ClassCastException if the value does not implement {@link Handled}
     * @throws NullPointerException if the specified value is {@code null}
     * @throws IllegalArgumentException if the value's associated {@link Handle} is not present in this map's associated
     * {@link Space}
     */
    @Override
    public Value putHandled(Value value) {
        if(value.isStatic()) {
            throw LogUtils.logExceptionAndGet(logger,
                    STATIC_VALUE_IN_MAP,
                    IllegalArgumentException::new);
        }
        if(value.isImmutable() != immutable) {
            throw LogUtils.logExceptionAndGet(logger,
                    immutableValueMismatch(immutable),
                    IllegalArgumentException::new);
        }
        Value old = map.putHandled(value);
        value.addModifiers(modifiers);
        removeModifiersFrom(old);
        return old;
    }

    /**
     * Returns an unmodifiable version of this map. All changes made to the original map will be reflected in the
     * returned one.
     *
     * @return unmodifiable version of this map
     */
    @Override
    public HandleMap<Value> unmodifiable() {
        return map.unmodifiable();
    }

    /**
     * Returns all values mapped to keys containing the specified tag. The tag may be a {@link Handle} or its
     * {@link String} id.
     *
     * @param tag tag {@link Handle} or its {@link String} id
     * @return all values mapped to keys containing the specified tag
     * @throws ClassCastException if the tag is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the tag is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} is not a tag
     */
    @Override
    public Collection<Value> getByTag(Object tag) {
        return map.getByTag(tag);
    }

    /**
     * Returns all values mapped to keys containing the specified tags. The tags may be {@link Handle Handles} or their
     * {@link String} ids.
     *
     * @param tags tag {@link Handle Handles} or their {@link String} ids
     * @return all values mapped to keys containing the specified tags
     * @throws ClassCastException if any of the tags are not {@link Handle Handles} or a {@link String Strings}
     * @throws NullPointerException if any of the tags is {@code null}
     * @throws IllegalArgumentException if any of the {@link Handle Handles} is not a tag
     */
    @Override
    public Collection<Value> getByTags(Collection<?> tags) {
        return map.getByTags(tags);
    }

    /**
     * Returns the number of {@link Value Values} in this map.
     *
     * @return number of {@link Value Values} in this map
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns {@code true} if this map contains no {@link Value Values}.
     *
     * @return {@code true} if this map contains no {@link Value Values}
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key. The key may either be a {@link Handle}
     * or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     * @throws ClassCastException if the key is of an inappropriate type for this map
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Returns {@code true} if this map contains the specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * Returns the {@link Value} to which the specified key is mapped, or {@code null} if none is present. The key may
     * either be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if none is present
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public Value get(Object key) {
        return map.get(key);
    }

    /**
     * Associates the specified {@link Value} with the specified key in this map. If the map previously contained a
     * mapping for the key, the old value is replaced by the specified value. The {@link Handle} key must be present in
     * this map's associated {@link Space}.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    public Value put(Handle key, Value value) {
        logger.warn(handleIgnored(key, value));
        return putHandled(value);
    }

    /**
     * Removes the mapping for a key from this map if it is present. Returns the value to which this map previously
     * associated the key, or {@code null} if the map contained no mapping for the key. The key may either be a
     * {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose mapping is to be removed from this map
     * @return the previous value associated with {@code key}, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public Value remove(Object key) {
        Value old = map.remove(key);
        removeModifiersFrom(old);
        return old;
    }

    /**
     * Copies all mappings from the specified map to this map. The effect of this call is equivalent to that of calling
     * {@link #put(Handle, Value)} on this map once for each mapping from key to {@link Value} in the specified map. All
     * {@link Handle} keys must be present in this map's associated {@link Space}.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation is not supported by this map
     * @throws NullPointerException if the specified map is {@code null} or contains {@code null} keys or values
     * @throws IllegalArgumentException if the specified map contains a {@link Handle} key that is not present in this
     * map's associated {@link Space}
     */
    @Override
    public void putAll(Map<? extends Handle, ? extends Value> m) {
        m.forEach(this::put);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key, or the specified default {@link Value}
     * if none is present. The key may either be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or the specified default value if this map contains no
     * mapping for the key
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public Value getOrDefault(Object key, Value defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    /**
     * Associates the specified key with the specified {@link Value} and returns {@code null} if the specified key is
     * not already associated with a value, otherwise returns the current value.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or {@code null} if there was no mapping for the key
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    public Value putIfAbsent(Handle key, Value value) {
        return map.putIfAbsent(key, value);
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to the specified {@link Value}. The key
     * may either be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key or value is {@code null}
     */
    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
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
     * Returns the {@link Handle Handles} of the {@link Value Values} associated with this map.
     *
     * @return {@link Handle Handles} of the {@link Value Values} associated with this map
     */
    @Override
    public Set<Handle> keySet() {
        return map.keySet();
    }

    /**
     * Returns the {@link Value Values} associated with this map.
     *
     * @return {@link Value Values} associated with this map
     */
    @Override
    public Collection<Value> values() {
        return map.values();
    }

    /**
     * Returns the {@link Handle Handles} and {@link Value Values} associated with this map.
     *
     * @return {@link Handle Handles} and {@link Value Values} associated with this map
     */
    @Override
    public Set<Entry<Handle,Value>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns a {@link String} representation of this map.
     *
     * @return {@link String} representation of this map
     */
    @Override
    public String toString() {
        return map.toString();
    }

    /**
     * Returns the hash code for this map.
     *
     * @return hash code for this map
     */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /**
     * Returns {@code true} if this map is equal to the specified object.
     *
     * @return {@code true} if this map is equal to the specified object
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DelegatedValueMap) {
            return map.equals(((DelegatedValueMap) obj).map);
        }
        if(obj instanceof Map) {
            return map.equals(obj);
        }
        return false;
    }

    private Stream<Value> getExisting(Collection<? extends Handle> handles) {
        return handles.stream().filter(this::containsKey).map(this::get);
    }

    private Stream<? extends Handle> getNonExisting(Collection<? extends Handle> handles) {
        if(handles.stream().anyMatch(Predicate.not(handle -> getSpace().equals(handle.getSpace())))) {
            logger.warn(spaceIgnored(getSpace()));
        }
        return handles.stream()
                .filter(Predicate.not(this::containsKey))
                .filter(handle -> getSpace().equals(handle.getSpace()));
    }

    private void createNonExisting(Collection<? extends Handle> handles, Numeral initialValue) {
        getNonExisting(handles).forEach(handle -> putHandled(new ValueImpl(handle, initialValue, immutable)));
    }

    private void contextOperation(Runnable operation, MathContext context) {
        checkImmutable();
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn(MIDWAY_EXCEPTION);
        }
        operation.run();
    }

    private void contextOperation(Consumer<Value> operation, Collection<? extends Handle> handles, MapMathContext context) {
        checkImmutable();
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn(MIDWAY_EXCEPTION);
        }

        if(context.createNonExistingAs() != null) {
            createNonExisting(handles, context.createNonExistingAs());
            values().forEach(operation);
        } else {
            getExisting(handles).forEach(operation);
        }
    }

    private void contextOperation(BiConsumer<Numeral, Value> operation, Map<? extends Handle, Numeral> values, MapMathContext context) {
        checkImmutable();
        if(context.convertResultTo() != null && !context.ignoreBadConversion()) {
            logger.warn(MIDWAY_EXCEPTION);
        }
        if(context.createNonExistingAs() != null) {
            createNonExisting(values.keySet(), context.createNonExistingAs());
        }
        getExisting(values.keySet())
                .forEach(val -> operation.accept(values.get(val.getHandle()), val));
    }

    private void checkImmutable() {
        if(immutable) {
            throw LogUtils.logExceptionAndGet(logger,
                    IMMUTABLE,
                    UnsupportedOperationException::new);
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
        private final ValueMap map;

        private final boolean modified;

        /**
         * Package-private constructor for {@link NumeralMap}.
         *
         * @param modified {@code true} if this map should reflect {@link Value Values} with modifiers attached
         */
        NumeralMap(boolean modified) {
            this.map = DelegatedValueMap.this;
            this.modified = modified;
        }

        /**
         * Returns the number of {@link Numeral Numerals} in this map.
         *
         * @return number of {@link Numeral Numerals} in this map
         */
        @Override
        public int size() {
            return DelegatedValueMap.this.size();
        }

        /**
         * Returns {@code true} if this map contains no {@link Numeral Numerals}.
         *
         * @return {@code true} if this map contains no {@link Numeral Numerals}
         */
        @Override
        public boolean isEmpty() {
            return DelegatedValueMap.this.isEmpty();
        }

        /**
         * Returns {@code true} if this map contains a mapping for the specified key.
         *
         * @param key key whose presence in this map is to be tested
         * @return {@code true} if this map contains a mapping for the specified key
         */
        @Override
        public boolean containsKey(Object key) {
            return DelegatedValueMap.this.containsKey(key);
        }

        /**
         * Returns {@code true} if this map maps one or more keys to the specified value.
         *
         * @param value value whose presence in this map is to be tested
         * @return {@code true} if this map maps one or more keys to the specified value
         */
        @Override
        public boolean containsValue(Object value) {
            return DelegatedValueMap.this.values().stream().map(this::getInternal).anyMatch(Predicate.isEqual(value));
        }

        /**
         * Returns the {@link Numeral} to which the specified key is mapped, or {@code null} if this map contains no
         * mapping for the key.
         *
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
         * Returns the {@link Handle Handles} of the {@link Numeral Numerals} associated with this map.
         *
         * @return {@link Handle Handles} of the {@link Numeral Numerals} associated with this map
         */
        @Override
        public Set<Handle> keySet() {
            return DelegatedValueMap.this.keySet();
        }

        /**
         * Returns the {@link Numeral Numerals} associated with this map.
         *
         * @return {@link Numeral Numerals} associated with this map
         */
        @Override
        public Collection<Numeral> values() {
            return DelegatedValueMap.this.values().stream().map(this::getInternal).collect(Collectors.toList());
        }

        /**
         * Returns the {@link Handle Handles} and {@link Numeral Numerals} associated with this map.
         *
         * @return {@link Handle Handles} and {@link Numeral Numerals} associated with this map
         */
        @Override
        public Set<Entry<Handle,Numeral>> entrySet() {
            return DelegatedValueMap.this.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), getInternal(entry.getValue()))).collect(Collectors.toSet());
        }

        /**
         * Returns a {@link String} representation of this map.
         *
         * @return {@link String} representation of this map
         */
        @Override
        public String toString() {
            return map.toString();
        }

        /**
         * Returns the hash code for this map.
         *
         * @return hash code for this map
         */
        @Override
        public int hashCode() {
            return map.hashCode();
        }

        /**
         * Returns {@code true} if this map is equal to the specified object.
         *
         * @return {@code true} if this map is equal to the specified object
         */
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof NumeralMap) {
                return map.equals(((NumeralMap) obj).map);
            }
            if(obj instanceof Map) {
                return map.equals(obj);
            }
            return false;
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
     * Creates a builder for a {@link DelegatedValueMap}.
     *
     * @param space {@link Space} to be associated with the map
     * @return builder with the specified {@link Space}
     */
    public static Builder builder(Space space) {
        return new Builder(space);
    }

    /**
     * A builder for {@link ValueMap}.
     */
    public static class Builder {
        private final Space space;
        private Logger logger;
        private BiFunction<Space, Logger, HandleMap<Value>> map;
        private final Set<Value> values;
        private final Set<Modifier> modifiers;
        private boolean immutable;
        private boolean forcedImmutable;
        private boolean determinedImmutable;

        private Builder(Space space) {
            this.space = space;
            logger = LoggerFactory.getLogger(DelegatedValueMap.class);
            map = HashHandleMap::new;
            values = new HashSet<>();
            modifiers = new HashSet<>();
            immutable = false;
            forcedImmutable = false;
            determinedImmutable = false;
        }

        /**
         * A {@link BiFunction} that accepts a {@link Space} and a {@link Logger} to create a {@link HandleMap} of
         * {@link Value Values} for the map.
         *
         * @param map {@link BiFunction} to initialize the backing {@link HandleMap}
         * @return this builder
         */
        public Builder map(BiFunction<Space, Logger, HandleMap<Value>> map) {
            this.map = map;
            return this;
        }

        /**
         * A {@link Logger} for the map.
         *
         * @param logger {@link Logger} for the map
         * @return this builder
         */
        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * {@code true} if the map should contain immutable {@link Value Values}.
         *
         * @param immutable {@code true} if the map should contain immutable {@link Value Values}
         * @return this builder
         *
         * @throws UnsupportedOperationException if this builder already contains {@link Value Values} that do not have
         * the specified mutability
         */
        public Builder immutable(boolean immutable) {
            if(forcedImmutable && this.immutable != immutable) {
                throw new UnsupportedOperationException(immutableValueMismatch(immutable));
            }
            this.immutable = immutable;
            determinedImmutable = true;
            return this;
        }

        /**
         * A {@link Value} to be associated with the map. All values must be of the same immutability, and an
         * {@link UnsupportedOperationException} is thrown if the mutability does not match. Immutability can also be
         * set explicitly with {@link #immutable(boolean)}.
         *
         * @param value {@link Value} to be associated with the map
         * @return this builder
         *
         * @throws UnsupportedOperationException if this builder already contains {@link Value Values} or has its
         * mutability explicitly set, and the mutability does not match the specified Value's mutability
         */
        public Builder value(Value value) {
            if((forcedImmutable || determinedImmutable) && value.isImmutable() != immutable) {
                throw new UnsupportedOperationException(immutableValueMismatch(immutable));
            }
            immutable = value.isImmutable();
            forcedImmutable = true;
            values.add(value);
            return this;
        }

        /**
         * {@link Value Values} to be associated with the map. All values must be of the same immutability, and an
         * {@link UnsupportedOperationException} is thrown if the mutability does not match. Immutability can also be
         * set explicitly with {@link #immutable(boolean)}.
         *
         * @param values {@link Value Values} to be associated with the map
         * @return this builder
         *
         * @throws UnsupportedOperationException if this builder already contains {@link Value Values} or has its
         * mutability explicitly set, and the mutability does not match the specified Value's mutability
         */
        public Builder values(Collection<? extends Value> values) {
            values.forEach(this::value);
            return this;
        }

        /**
         * Removes all {@link Value Values} from the map.
         *
         * @return this builder
         */
        public Builder clearValues() {
            values.clear();
            forcedImmutable = false;
            return this;
        }

        /**
         * A {@link Modifier} to be associated with the map.
         *
         * @param modifier {@link Modifier} to be associated with the map
         * @return this builder
         */
        public Builder modifier(Modifier modifier) {
            modifiers.add(modifier);
            return this;
        }

        /**
         * {@link Modifier Modifiers} to be associated with the map.
         *
         * @param modifiers {@link Modifier Modifiers} to be associated with the map
         * @return this builder
         */
        public Builder modifiers(Collection<? extends Modifier> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        /**
         * Removes all {@link Modifier Modifiers} from the map.
         *
         * @return this builder
         */
        public Builder clearModifiers() {
            modifiers.clear();
            return this;
        }

        /**
         * Builds the map.
         *
         * @return map initialized by this builder
         */
        public ValueMap build() {
            ValueMap map = new DelegatedValueMap(this.map.apply(space, logger), immutable, logger);
            values.forEach(map::putHandled);
            map.addModifiers(modifiers);
            return map;
        }
    }
}
