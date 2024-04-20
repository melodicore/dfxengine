package me.datafox.dfxengine.handles;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * <p>
 * Configuration for {@link HandleManagerImpl}. Contains options on whether to use unordered ({@link HashHandleSet} and
 * {@link HashHandleMap}) or ordered ({@link TreeHandleSet} and {@link TreeHandleMap}) collections in various parts of
 * the Handles module. It also registers a default configuration as a {@link Component} that can be overridden when
 * using the Injector module.
 * </p>
 * <p>
 * Summary:
 * <ul>
 *     <li>{@link #orderedSpaces} reflects {@link HandleManagerImpl#getSpaces()} and is {@code true} by default</li>
 *     <li>{@link #orderedHandlesInSpaces} reflects {@link SpaceImpl#getHandles()} and {@link SpaceImpl#getAllHandles()}
 *     and is {@code true} by default</li>
 *     <li>{@link #orderedHandlesInGroups} reflects {@link GroupImpl#getHandles()} and is {@code false} by default</li>
 *     <li>{@link #orderedGroups} reflects {@link SpaceImpl#getGroups()} and is {@code true} by default</li>
 *     <li>{@link #orderedSubHandles} reflects {@link HandleImpl#getSubHandles()} and is {@code true} by default</li>
 *     <li>{@link #orderedTags} reflects {@link HandleImpl#getTags()} and is {@code false} by default</li>
 * </ul>
 * </p>
 * @author datafox
 */
@Data
@Builder
public class HandleManagerConfiguration {
    /**
     * {@link HandleManagerImpl#getSpaces()} will return a {@link TreeHandleMap} if {@code true} and a
     * {@link HashHandleMap} if {@code false}. Is {@code true} by default.
     */
    @Builder.Default
    private final boolean orderedSpaces = true;

    /**
     * {@link SpaceImpl#getHandles()} and {@link SpaceImpl#getAllHandles()} will return a {@link TreeHandleSet} if
     * {@code true} and a {@link HashHandleSet} if {@code false}. Is {@code true} by default.
     */
    @Builder.Default
    private final boolean orderedHandlesInSpaces = true;

    /**
     * {@link GroupImpl#getHandles()} will return a {@link TreeHandleSet} if {@code true} and a {@link HashHandleSet} if
     * {@code false}. Is {@code false} by default.
     */
    @Builder.Default
    private final boolean orderedHandlesInGroups = false;

    /**
     * {@link SpaceImpl#getGroups()} will return a {@link TreeHandleMap} if {@code true} and a {@link HashHandleMap} if
     * {@code false}. Is {@code true} by default.
     */
    @Builder.Default
    private final boolean orderedGroups = true;

    /**
     * {@link HandleImpl#getSubHandles()} will return a {@link TreeHandleSet} if {@code true} and a
     * {@link HashHandleSet} if {@code false}. Is {@code true} by default.
     */
    @Builder.Default
    private final boolean orderedSubHandles = true;

    /**
     * {@link HandleImpl#getTags()} will return a {@link TreeHandleSet} if {@code true} and a {@link HashHandleSet} if
     * {@code false}. Is {@code true} by default.
     */
    @Builder.Default
    private final boolean orderedTags = false;

    /**
     * @return default configuration (see {@link HandleManagerConfiguration})
     */
    @Component(order = Integer.MAX_VALUE)
    public static HandleManagerConfiguration defaultConfiguration() {
        return builder().build();
    }
}
