package me.datafox.dfxengine.handles.collection;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Implementation of {@link HandleSet} backed with a {@link HashSet}.
 *
 * @author datafox
 */
public class HashHandleSet extends HashSet<Handle> implements HandleSet {
    private final Logger logger;

    @Getter
    private final Space space;

    private final Map<String,Handle> idMap;

    public HashHandleSet(Space space) {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
        this.space = space;
        idMap = new HashMap<>();
    }

    public HashHandleSet(Space space, Collection<Handle> handles) {
        this(space);
        addAll(handles);
    }

    /**
     * In addition to what is documented in {@link HashSet#add(Object)}, the specified {@link Handle} must be contained
     * within the {@link Space} associated with this set.
     *
     * @param handle element whose presence in this collection is to be ensured
     * @return true if this set did not already contain the specified element
     */
    @Override
    public boolean add(Handle handle) {
        checkSpace(handle);

        return addInternal(handle);
    }

    @Override
    public boolean remove(Object o) {
        boolean returned = super.remove(o);

        if(returned && o instanceof Handle) {
            idMap.remove(((Handle) o).getId(), o);
        }

        return returned;
    }

    /**
     * In addition to what is documented in {@link HashSet#addAll(Collection)}, all {@link Handle}s specified must be
     * contained within the {@link Space} associated with this set.
     *
     * @param c collection containing Handles to be added to this set
     * @return true if this set changed as a result of the call
     */
    @Override
    public boolean addAll(Collection<? extends Handle> c) {
        c.forEach(this::checkSpace);

        return super.addAll(c);
    }

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

    @Override
    public void clear() {
        idMap.clear();
        super.clear();
    }

    @Override
    public boolean containsById(String id) {
        return idMap.containsKey(id);
    }

    @Override
    public boolean containsAllById(Collection<String> ids) {
        return ids.stream().allMatch(idMap::containsKey);
    }

    @Override
    public Handle get(String id) {
        return idMap.get(id);
    }

    @Override
    public boolean removeById(String id) {
        Handle handle = idMap.remove(id);

        if(handle != null) {
            this.remove(handle);

            return true;
        }

        return false;
    }

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
