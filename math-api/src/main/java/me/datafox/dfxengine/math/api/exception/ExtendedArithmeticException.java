package me.datafox.dfxengine.math.api.exception;

/**
 * Thrown when an exceptional arithmetic condition has occurred. This class exists because {@link ArithmeticException}
 * does not support causes or stack traces.
 *
 * @author datafox
 */
public class ExtendedArithmeticException extends RuntimeException {
    /**
     * Constructor for ExtendedArithmeticException
     */
    public ExtendedArithmeticException() {
        super();
    }

    /**
     * Constructor for ExtendedArithmeticException
     *
     * @param message message for the exception
     */
    public ExtendedArithmeticException(String message) {
        super(message);
    }

    /**
     * Constructor for ExtendedArithmeticException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public ExtendedArithmeticException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ExtendedArithmeticException
     *
     * @param cause cause for the exception
     */
    public ExtendedArithmeticException(Throwable cause) {
        super(cause);
    }
}
