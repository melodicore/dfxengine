package me.datafox.dfxengine.entities.api;

/**
 * @author datafox
 */
public interface SerializationHandler {
    String serialize(Object object);

    <T> T deserialize(Class<T> type, String data);
}
