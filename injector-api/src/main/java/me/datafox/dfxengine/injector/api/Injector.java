package me.datafox.dfxengine.injector.api;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.exception.ParameterCountMismatchException;

import java.util.List;

/**
 * @author datafox
 */
public interface Injector {
    /**
     * Returns a single {@link Component} of the specified type. If the {@link Class} has type parameters, use
     * {@link #getComponent(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link Class} has type parameters
     */
    <T> T getComponent(Class<T> type);

    /**
     * Returns a single {@link Component} of the specified type.
     *
     * @param type type of the {@link Component} to be requested
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     */
    <T> T getComponent(TypeRef<T> type);

    /**
     * Returns a single {@link Component} of the specified type. If the Component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. If the {@link Class} has type
     * parameters, use {@link #getComponent(TypeRef)} instead.
     *
     * @param type type of the {@link Component} to be requested
     * @param requesting type of the object requesting the {@link Component}
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     * @param <R> type of the object requesting the {@link Component}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link Class} has type parameters
     */
    <T,R> T getComponent(Class<T> type, Class<R> requesting);

    /**
     * Returns a single {@link Component} of the specified type. If the Component has
     * {@link InstantiationPolicy#PER_INSTANCE} and depends on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component} to be requested
     * @param requesting type of the object requesting the {@link Component}
     * @return {@link Component} of the specified type
     * @param <T> type of the {@link Component} to be requested
     * @param <R> type of the object requesting the {@link Component}
     */
    <T,R> T getComponent(TypeRef<T> type, TypeRef<R> requesting);

    /**
     * Returns {@link Component Components} of the specified type. If the {@link Class} has type parameters, use
     * {@link #getComponents(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component Components} to be requested
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     *
     * @throws ParameterCountMismatchException if the {@link Class} has type parameters
     */
    <T> List<T> getComponents(Class<T> type);

    /**
     * Returns {@link Component Components} of the specified type.
     *
     * @param type type of the {@link Component Components} to be requested
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     */
    <T> List<T> getComponents(TypeRef<T> type);

    /**
     * Returns {@link Component Components} of the specified type. If any of the Components have
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}. If the {@link Class} has type
     * parameters, use {@link #getComponent(TypeRef)} instead. Otherwise, an exception is thrown.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     *
     * @throws ParameterCountMismatchException if the type or requesting {@link Class} has type parameters
     */
    <T,R> List<T> getComponents(Class<T> type, Class<R> requesting);

    /**
     * Returns {@link Component Components} of the specified type. If any of the Components hav
     * {@link InstantiationPolicy#PER_INSTANCE} and depend on {@link InstantiationDetails}, the {@code requesting}
     * parameter will be used for {@link InstantiationDetails#getRequesting()}.
     *
     * @param type type of the {@link Component Components} to be requested
     * @param requesting type of the object requesting the {@link Component Components}
     * @return {@link List} of {@link Component Components} of the specified type
     * @param <T> type of the {@link Component Components} to be requested
     * @param <R> type of the object requesting the {@link Component Components}
     */
    <T,R> List<T> getComponents(TypeRef<T> type, TypeRef<R> requesting);
}
