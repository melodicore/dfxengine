package me.datafox.dfxengine.entities.api.component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author datafox
 */
public interface SerializationHandler<B, T> {
    T getSerializer();

    String serialize(Object object);

    void serialize(Object object, OutputStream stream);

    <E> E deserialize(Class<E> type, String data);

    <E> E deserialize(Class<E> type, InputStream stream);
}
