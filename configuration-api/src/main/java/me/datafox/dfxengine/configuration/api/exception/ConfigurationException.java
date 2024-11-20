package me.datafox.dfxengine.configuration.api.exception;

import me.datafox.dfxengine.configuration.api.Configuration;

/**
 * Thrown if a {@link Configuration} has an invalid value for the component using it.
 *
 * @author datafox
 */
public class ConfigurationException extends RuntimeException {
    /**
     * Constructor for {@link ConfigurationException}.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Constructor for {@link ConfigurationException}.
     *
     * @param message message for the exception
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor for {@link ConfigurationException}.
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for {@link ConfigurationException}.
     *
     * @param cause cause for the exception
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
