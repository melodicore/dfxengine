package me.datafox.dfxengine.handles.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * An extension of {@link TreeSet} that can only contain {@link Handle Handles} of the {@link Space} associated with
 * this set. Implements {@link HandleSet} for various helper methods related to Handles.
 *
 * @author datafox
 */
public class TreeHandleSet extends TreeSet<Handle> implements HandleSet {
    private final Logger logger;

    private final Space space;

    private final Map<String,Handle> idMap;

    /**
     * @param space {@link Space} to be associated with this set
     */
    public TreeHandleSet(Space space) {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
        this.space = space;
        idMap = new HashMap<>();
    }

    /**
     * @param space {@link Space} to be associated with this set
     * @param handles collection of {@link Handle Handles} that are to be placed into this set
     */
    public TreeHandleSet(Space space, Collection<Handle> handles) {
        this(space);
        addAll(handles);
    }

    /**
     * Adds the specified {@link Handle} to this set if it is not already present. If this set already contains the
     * Handle, this set is left unchanged and {@code false} is returned. The specified Handle must be present in
     * the {@link Space} associated with this set.
     *
     * @param handle {@link Handle} whose presence in this set is to be ensured
     * @return {@code true} if this set did not already contain the specified {@link Handle}
     *
     * @throws IllegalArgumentException if the specified {@link Handle} is not contained in the {@link Space}
     * associated with this set
     */
    @Override
    public boolean add(Handle handle) {
        checkSpace(handle);

        return addInternal(handle);
    }

    /**
     * Removes the specified {@link Handle} from this set if it is present. Returns {@code true} if this set contained
     * the specified Handle.
     *
     * @param handle {@link Handle} to be removed from this set, if present
     * @return {@code true} if the set contained the specified {@link Handle}
     */
    @Override
    public boolean remove(Object handle) {
        boolean returned = super.remove(handle);

        if(returned && handle instanceof Handle) {
            idMap.remove(((Handle) handle).getId(), handle);
        }

        return returned;
    }

    /**
     * Adds all of the {@link Handle Handles} in the specified collection to this set if they're not already present.
     * All {@link Handle Handles} specified must be present in the {@link Space} associated with this set.
     *
     * @param c collection containing {@link Handle Handles} to be added to this set
     * @return {@code true} if this set changed as a result of the call
     *
     * @throws IllegalArgumentException if any the specified {@link Handle Handles} are not contained in the
     * {@link Space} associated with this set
     */
    @Override
    public boolean addAll(Collection<? extends Handle> c) {
        c.forEach(this::checkSpace);

        boolean changed = false;

        for(Handle handle : c) {
            if(addInternal(handle)) {
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Removes from this set all of its {@link Handle Handles} that are contained in the specified collection.
     *
     * @param  c collection containing {@link Handle Handles} to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;

        for(Object o : c) {
            if(remove(o)) {
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Removes all of the {@link Handle Handles} from this set. The set will be empty after this call returns.
     */
    @Override
    public void clear() {
        idMap.clear();
        super.clear();
    }

    /**
     * @return {@link Space} associated with this set
     */
    @Override
    public Space getSpace() {
        return space;
    }

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this set contains a {@link Handle} with the specified id
     */
    @Override
    public boolean containsById(String id) {
        return idMap.containsKey(id);
    }

    /**
     * @param ids ids of the {@link Handle Handles} to be checked for
     * @return {@code true} if this set contains all {@link Handle Handles} with the specified ids
     */
    @Override
    public boolean containsAllById(Collection<String> ids) {
        return ids.stream().allMatch(idMap::containsKey);
    }

    /**
     * @param id id of the requested {@link Handle}
     * @return {@link Handle} matching the specified id, or {@code null} if none are present
     */
    @Override
    public Handle get(String id) {
        return idMap.get(id);
    }

    /**
     * @param id id of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this set changed as a result of this action
     */
    @Override
    public boolean removeById(String id) {
        Handle handle = idMap.remove(id);

        if(handle != null) {
            this.remove(handle);

            return true;
        }

        return false;
    }

    /**
     * @param ids ids of the {@link Handle Handles} to be removed
     * @return {@code true} if the {@link Handle Handles} of this set changed as a result of this action
     */
    @Override
    public boolean removeAllById(Collection<String> ids) {
        boolean changed = false;

        for(String id : ids) {
            if(removeById(id)) {
                changed = true;
            }
        }
        return changed;
    }

    private void checkSpace(Handle handle) {
        if(!space.equals(handle.getSpace())) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.spaceMismatchHandleSet(handle, space),
                    IllegalArgumentException::new);
        }
    }

    private boolean addInternal(Handle handle) {
        if(!super.add(handle)) {
            return false;
        }

        idMap.put(handle.getId(), handle);

        return true;
    }
}
