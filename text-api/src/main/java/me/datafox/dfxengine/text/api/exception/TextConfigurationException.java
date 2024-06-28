package me.datafox.dfxengine.text.api.exception;

import me.datafox.dfxengine.text.api.TextConfiguration;

/**
 * Thrown if a {@link TextConfiguration} has an invalid value for the component using it.
 *
 * @author datafox
 */
public class TextConfigurationException extends RuntimeException {
    /**
     * Constructor for TextConfigurationException
     */
    public TextConfigurationException() {
        super();
    }

    /**
     * Constructor for TextConfigurationException
     *
     * @param message message for the exception
     */
    public TextConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor for TextConfigurationException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public TextConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for TextConfigurationException
     *
     * @param cause cause for the exception
     */
    public TextConfigurationException(Throwable cause) {
        super(cause);
    }
}
