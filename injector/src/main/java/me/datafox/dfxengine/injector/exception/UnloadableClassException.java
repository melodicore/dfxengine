package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when the {@link Class} represented by an object in
 * {@link me.datafox.dfxengine.injector.serialization} cannot be loaded.
 *
 * @author datafox
 */
public class UnloadableClassException extends RuntimeException {
    /**
     * Public constructor for {@link UnloadableClassException}.
     *
     * @param message details for this exception
     */
    public UnloadableClassException(String message) {
        super(message);
    }
}
