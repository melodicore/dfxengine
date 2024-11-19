package me.datafox.dfxengine.handles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.*;
import me.datafox.dfxengine.handles.utils.HandleUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import static me.datafox.dfxengine.handles.utils.HandleStrings.*;

/**
 * Implementation of {@link Handle}.
 *
 * @author datafox
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HandleImpl implements Handle {
    /**
     * {@link HandleManager} managing this handle.
     */
    @EqualsAndHashCode.Include
    private final HandleManager handleManager;

    /**
     * {@link Logger} of this handle.
     */
    private final Logger logger;

    /**
     * {@link Space} that this handle is contained in.
     */
    @EqualsAndHashCode.Include
    private final Space space;

    /**
     * {@link String} id of this handle.
     */
    private final String id;

    /**
     * Index of this handle.
     */
    @EqualsAndHashCode.Include
    private final int index;

    /**
     * Sub-index of this handle.
     */
    @EqualsAndHashCode.Include
    private final int subIndex;

    private final HandleSet subHandles;

    /**
     * {@link HandleSet} containing the tags of this handle.
     */
    private final HandleSet tags;

    /**
     * Package-private constructor for {@link HandleImpl}.
     *
     * @param space {@link Space} containing this handle
     * @param id {@link String} id for this handle
     * @param index index of this handle in the space
     * @param subIndex sub-index for this handle
     */
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
     * Returns the {@link HandleSet} containing the subhandles of this handle, or an empty set if this handle is a
     * subhandle.
     *
     * @return {@link HandleSet} containing the subhandles of this handle
     */
    @Override
    public HandleSet getSubHandles() {
        return subHandles.unmodifiable();
    }

    /**
     * Creates a new subhandle. Throws {@link UnsupportedOperationException} if this handle is a subhandle or is
     * associated with a {@link Space} (subhandles for space handles are used exclusively for {@link Group Groups},
     * use {@link Space#createGroup(String)} instead).
     *
     * @param id {@link String} id for the new subhandle
     * @return created subhandle
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a subhandle with the given id already exists in this handle
     * @throws UnsupportedOperationException if this handle is a subhandle or if this handle is associated with a
     * {@link Space}
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
     * Creates a subhandle with the specified id if it does not already exist and returns the subhandle with that id.
     * Throws {@link UnsupportedOperationException} if this handle is a subhandle or is associated with a {@link Space}
     * (subhandles for space handles are used exclusively for {@link Group Groups}) and a subhandle with the specified
     * id is not present. Use {@link Space#getOrCreateGroup(String)}.
     *
     * @param id {@link String} id for the subhandle
     * @return created or pre-existing subhandle
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     * @throws UnsupportedOperationException if this handle is a subhandle or if this handle is associated with a
     * {@link Space} and a subhandle with the specified id is not present
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
     * Returns {@code true} if this handle is a subhandle.
     *
     * @return {@code true} if this handle is a subhandle
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

    /**
     * Returns the {@link String} representation of this handle.
     *
     * @return {@link String} representation of this handle
     */
    @Override
    public String toString() {
        return String.format("%s@%s", getId(), space.getHandle().getId());
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
