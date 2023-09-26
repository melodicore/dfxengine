package me.datafox.dfxengine.math.api.exception;

/**
 * Thrown when an exceptional arithmetic condition has occurred. This class exists because {@link ArithmeticException}
 * does not support causes or stack traces.
 *
 * @author datafox
 */
public class ExtendedArithmeticException extends RuntimeException {
    public ExtendedArithmeticException() {
        super();
    }

    public ExtendedArithmeticException(String message) {
        super(message);
    }

    public ExtendedArithmeticException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtendedArithmeticException(Throwable cause) {
        super(cause);
    }

    protected ExtendedArithmeticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
