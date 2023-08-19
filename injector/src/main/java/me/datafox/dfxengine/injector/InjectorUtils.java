package me.datafox.dfxengine.injector;

import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.MultipleInjectConstructorsException;
import me.datafox.dfxengine.injector.exception.NoValidConstructorException;
import me.datafox.dfxengine.utils.ClassUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Utilities used internally by {@link InjectorBuilder} and {@link Injector}.
 *
 * @author datafox
 */
class InjectorUtils {
    static Class<?> getListType(Type type, Logger logger) {
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            if(parameterizedType.getActualTypeArguments().length != 0) {
                try {
                    Class<?> classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    if(parameterizedType.getActualTypeArguments().length > 1) {
                        logger.warn(InjectorStrings.maybeOddListType(classType));
                    }

                    return classType;
                } catch(ClassCastException ignored) {
                }

                try {
                    ParameterizedType parameterizedType1 = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];

                    Class<?> classType = (Class<?>) parameterizedType1.getRawType();

                    logger.warn(InjectorStrings.parameterizedListType(classType));

                    return classType;
                } catch(ClassCastException ignored) {
                }
            }
        }

        logger.warn(InjectorStrings.ODD_LIST_TYPE);

        return Object.class;
    }

    static <T> Constructor<T> getConstructor(Class<T> type, Logger logger) {
        logger.debug(InjectorStrings.validConstructors(type));

        List<Constructor<T>> constructors = ClassUtils.getConstructorsWithAnnotation(type, Inject.class);

        if(constructors.isEmpty()) {
            Constructor<T> constructor;
            try {
                constructor = type.getDeclaredConstructor();
            } catch(NoSuchMethodException e) {
                throw LogUtils.logExceptionAndGet(logger, InjectorStrings.noValidConstructor(type),
                        e, NoValidConstructorException::new);
            }
            logger.debug(InjectorStrings.DEFAULT_CONSTRUCTOR);
            return constructor;
        }

        if(constructors.size() != 1) {
            throw LogUtils.logExceptionAndGet(logger, InjectorStrings.multipleInjectConstructors(type),
                    MultipleInjectConstructorsException::new);
        }

        Constructor<T> constructor = constructors.get(0);

        logger.debug(InjectorStrings.validConstructorFound(constructor));

        return constructor;
    }
}
