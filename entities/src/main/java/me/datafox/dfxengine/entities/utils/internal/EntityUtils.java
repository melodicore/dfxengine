package me.datafox.dfxengine.entities.utils.internal;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.EntityData;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.data.ValueDto;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.Numerals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class EntityUtils {
    public static Numeral getNumeral(String type, String value) {
        switch(NumeralType.valueOf(type)) {
            case INT:
                return Numerals.of(Integer.valueOf(value));
            case LONG:
                return Numerals.of(Long.valueOf(value));
            case BIG_INT:
                return Numerals.of(new BigInteger(value));
            case FLOAT:
                return Numerals.of(Float.valueOf(value));
            case DOUBLE:
                return Numerals.of(Double.valueOf(value));
            case BIG_DEC:
                return Numerals.of(new BigDecimal(value));
            default:
                throw new IllegalArgumentException("unknown type");
        }
    }

    public static Handle getValueHandle(Space space, ValueDto value) {
        return space.getAllHandles().get(value.getHandle());
    }

    public static Numeral getValueNumeral(ValueDto value) {
        return getNumeral(value.getValueType(), value.getValue());
    }

    public static Stream<EntityData<?>> flatMapEntityData(Engine engine,
                                                      Reference<EntityComponent> component,
                                                      String type,
                                                      Function<HandleMap<EntityData<?>>,Stream<EntityData<?>>> mapper) {
        return component
                .get(engine)
                .map(EntityComponent::getData)
                .map(map -> map.get(type))
                .filter(Objects::nonNull)
                .flatMap(mapper);
    }

    public static <T> Stream<T> assertSingleAndStream(Engine engine, List<Reference<T>> references) {
        if(!references.stream().allMatch(Reference::isSingle)) {
            throw new IllegalArgumentException("DataReferences must be single");
        }
        return references
                .stream()
                .map(d -> d.get(engine))
                .map(Stream::findFirst)
                .map(o -> o.orElseThrow(() -> new IllegalArgumentException("Data not found")));
    }
}
