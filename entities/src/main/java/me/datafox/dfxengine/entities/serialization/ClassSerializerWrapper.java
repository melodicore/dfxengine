package me.datafox.dfxengine.entities.serialization;

import com.esotericsoftware.jsonbeans.JsonSerializer;
import lombok.Data;

/**
 * @author datafox
 */
@Data
public final class ClassSerializerWrapper<T> {
    private final Class<T> type;

    private final JsonSerializer<T> serializer;
}
