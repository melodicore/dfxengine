package me.datafox.dfxengine.handles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.utils.HandleUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import static me.datafox.dfxengine.handles.utils.HandleStrings.*;

/**
 * @author datafox
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HandleImpl implements Handle {
    @EqualsAndHashCode.Include
    private final HandleManager handleManager;
    private final Logger logger;
    @EqualsAndHashCode.Include
    private final Space space;
    private final String id;
    @EqualsAndHashCode.Include
    private final int index;
    @EqualsAndHashCode.Include
    private final int subIndex;
    private final HandleSet subHandles;
    private final HandleSet tags;

    HandleImpl(Space space, String id, int index, int subIndex) {
        handleManager = space.getHandleManager();
        logger = ((HandleManagerImpl) handleManager).getLogger();
        HandleManagerConfiguration conf = ((HandleManagerImpl) handleManager).getConfiguration();
        this.space = space;
        this.id = id;
        this.index = index;
        this.subIndex = subIndex;
        subHandles = conf.isOrderedSubHandles() ?
                new TreeHandleSet(space, logger) :
                new HashHandleSet(space, logger);
        tags = conf.isOrderedTags() ?
                new TreeHandleSet(handleManager.getTagSpace(), logger) :
                new HashHandleSet(handleManager.getTagSpace(), logger);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleSet getSubHandles() {
        return subHandles.unmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    @Override
    public Handle createSubHandle(String id) {
        checkSubHandle();
        HandleUtils.checkId(SUBHANDLE, id, logger);
        checkSubHandleId(id);
        id = this.id + ":" + id;
        if(subHandles.contains(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    unavailableId(SUBHANDLE, id),
                    IllegalArgumentException::new);
        }
        return createSubHandleInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    @Override
    public Handle getOrCreateSubHandle(String id) {
        checkSubHandle();
        HandleUtils.checkId(SUBHANDLE, id, logger);
        checkSubHandleId(id);
        id = this.id + ":" + id;
        if(subHandles.contains(id)) {
            return subHandles.get(id);
        }
        return createSubHandleInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isSubHandle() {
        return subIndex != -1;
    }

    /**
     * Compares this handle with the specified handle for order. Returns a negative integer, zero, or a positive integer
     * as this handle is less than, equal to, or greater than the specified handle. The order of handles is specified by
     * three things. If the {@link Space Spaces} of the handles are different, comparison is done with the space's
     * handle. If the spaces are the same, comparison is done with the index, using the sub-index if the indices are
     * also the same.
     *
     * @param o handle to be compared.
     * @return negative integer, zero, or a positive integer as this handle is less than, equal to, or greater than the
     * specified handle
     * @throws NullPointerException if the specified handle is {@code null}
     */
    @Override
    public int compareTo(Handle o) {
        if(!space.equals(o.getSpace())) {
            return space.getHandle().compareTo(o.getSpace().getHandle());
        }
        if(index != o.getIndex()) {
            return Integer.compare(index, o.getIndex());
        }
        return Integer.compare(subIndex, o.getSubIndex());
    }

    @Override
    public String toString() {
        return String.format("%s > %s", space.getHandle().getId(), getId());
    }

    private void checkSubHandle() {
        if(isSubHandle()) {
            throw LogUtils.logExceptionAndGet(logger,
                    SUBHANDLE_SUBHANDLE,
                    UnsupportedOperationException::new);
        }
    }

    private void checkSubHandleId(String id) {
        if(id.contains(":")) {
            throw LogUtils.logExceptionAndGet(logger,
                    SUBHANDLE_ID,
                    IllegalArgumentException::new);
        }
    }

    private Handle createSubHandleInternal(String id) {
        Handle handle = new HandleImpl(space, id, index, subHandles.size());
        subHandles.add(handle);
        return handle;
    }
}
