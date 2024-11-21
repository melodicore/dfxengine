package me.datafox.dfxengine.entities.exception;

/**
 * @author datafox
 */
public class NoStateConverterException extends RuntimeException {
    public NoStateConverterException() {
        super();
    }

    public NoStateConverterException(String message) {
        super(message);
    }

    public NoStateConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStateConverterException(Throwable cause) {
        super(cause);
    }

    protected NoStateConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
