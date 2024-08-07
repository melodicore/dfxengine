package me.datafox.dfxengine.handles;

import lombok.AccessLevel;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.*;
import me.datafox.dfxengine.handles.utils.HandleUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static me.datafox.dfxengine.handles.utils.HandleStrings.*;

/**
 * Implementation of {@link HandleManager}.
 *
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandleManagerImpl implements HandleManager {
    @Getter(AccessLevel.PACKAGE)
    private final Logger logger;
    @Getter
    private final Space spaceSpace;
    @Getter
    private final Space tagSpace;
    private final HandleMap<Space> spaces;
    @Getter(AccessLevel.PACKAGE)
    private final HandleManagerConfiguration configuration;

    /**
     * @param logger {@link Logger} for the handle manager
     * @param configuration {@link HandleManagerConfiguration} for the handle manager
     */
    @Inject
    public HandleManagerImpl(Logger logger, HandleManagerConfiguration configuration) {
        logger.info(INITIALIZING);
        this.logger = logger;
        this.configuration = configuration;
        logConfiguration();
        spaceSpace = new SpaceSpace();
        spaces = configuration.isOrderedSpaces() ?
                new TreeHandleMap<>(spaceSpace, logger) :
                new HashHandleMap<>(spaceSpace, logger);
        tagSpace = createSpace(TAG_SPACE_ID);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleMap<Space> getSpaces() {
        return spaces.unmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Space createSpace(String id) {
        HandleUtils.checkId(SPACE, id, logger);
        if(spaces.containsKey(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    unavailableId(SPACE, id),
                    IllegalArgumentException::new);
        }
        return createSpaceInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Space getOrCreateSpace(String id) {
        HandleUtils.checkId(SPACE, id, logger);
        if(spaces.containsKey(id)) {
            return spaces.get(id);
        }
        return createSpaceInternal(id);
    }

    private Space createSpaceInternal(String id) {
        Handle handle = ((SpaceSpace) spaceSpace).createHandleInternal(id);
        SpaceImpl space = new SpaceImpl(handle, this);
        spaces.put(handle, space);
        return space;
    }

    private class SpaceSpace extends SpaceImpl {
        private final HandleSet handles;

        SpaceSpace() {
            super(null, HandleManagerImpl.this);
            try {
                Field handlesField = SpaceImpl.class.getDeclaredField("handles");
                handlesField.trySetAccessible();
                handles = (HandleSet) handlesField.get(this);

                Handle handle = createHandleInternal(SPACE_SPACE_ID);

                Field handleField = SpaceImpl.class.getDeclaredField("handle");
                handleField.trySetAccessible();
                handleField.set(this, handle);
            } catch(NoSuchFieldException | IllegalAccessException e) {
                throw LogUtils.logExceptionAndGet(logger,
                        REFLECTION,
                        e,
                        RuntimeException::new);
            }
        }

        @Override
        public Handle createHandle(String id) {
            throw LogUtils.logExceptionAndGet(logger,
                    CREATE_SPACE_HANDLE,
                    UnsupportedOperationException::new);
        }

        @Override
        public Handle getOrCreateHandle(String id) {
            if(handles.contains(id)) {
                return handles.get(id);
            }
            throw LogUtils.logExceptionAndGet(logger,
                    GET_CREATE_SPACE_HANDLE,
                    UnsupportedOperationException::new);
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            return HandleManagerImpl.this.hashCode() + 1;
        }

        Handle createHandleInternal(String id) {
            Handle handle = new SpaceHandle(this, id, handles.size(), -1);
            handles.add(handle);
            return handle;
        }

    }
    class SpaceHandle extends HandleImpl {

        SpaceHandle(Space space, String id, int index, int subIndex) {
            super(space, id, index, subIndex);
        }
        @Override
        public Handle createSubHandle(String id) {
            throw LogUtils.logExceptionAndGet(logger,
                    CREATE_SPACE_SUBHANDLE,
                    UnsupportedOperationException::new);
        }

        @Override
        public Handle getOrCreateSubHandle(String id) {
            id = getId() + ":" + id;
            if(getSubHandles().contains(id)) {
                return getSubHandles().get(id);
            }
            throw LogUtils.logExceptionAndGet(logger,
                    GET_CREATE_SPACE_SUBHANDLE,
                    UnsupportedOperationException::new);
        }

        Handle createSubHandleInternal(String id) {
            return super.createSubHandle(id);
        }

    }

    private void logConfiguration() {
        if(!logger.isDebugEnabled()) {
            return;
        }
        logger.debug(spaceConfiguration(configuration.isOrderedSpaces()));
        logger.debug(handleInSpaceConfiguration(configuration.isOrderedHandlesInSpaces()));
        logger.debug(handleInGroupConfiguration(configuration.isOrderedHandlesInGroups()));
        logger.debug(groupConfiguration(configuration.isOrderedGroups()));
        logger.debug(subhandleConfiguration(configuration.isOrderedSubHandles()));
        logger.debug(tagConfiguration(configuration.isOrderedTags()));
    }
}
