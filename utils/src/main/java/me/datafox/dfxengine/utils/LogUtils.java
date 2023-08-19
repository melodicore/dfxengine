package me.datafox.dfxengine.utils;

import org.slf4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utilities for logging, mainly for logging exceptions
 *
 * @author datafox
 */
public class LogUtils {
    /**
     * Logs an error message and throws an exception with that message.
     *
     * @param logger logger
     * @param message exception message
     * @param cause exception cause
     * @param constructor function to instantiate the exception
     * @return exception instantiated with the provided function
     */
    public static <T extends Throwable> T logExceptionAndGet(Logger logger,
                                                             String message,
                                                             Throwable cause,
                                                             BiFunction<String,Throwable,T> constructor) {
        logger.error(message);
        return constructor.apply(message, cause);
    }

    /**
     * Logs an error message and throws an exception with that message.
     *
     * @param logger logger
     * @param message exception message
     * @param constructor function to instantiate the exception
     * @return exception instantiated with the provided function
     */
    public static <T extends Throwable> T logExceptionAndGet(Logger logger,
                                                             String message,
                                                             Function<String,T> constructor) {
        logger.error(message);
        return constructor.apply(message);
    }
}
