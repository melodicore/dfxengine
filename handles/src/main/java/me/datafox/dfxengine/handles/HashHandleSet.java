package me.datafox.dfxengine.handles;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.utils.UnmodifiableHandleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.utils.HandleUtils.*;

/**
 * An unordered implementation of {@link HandleSet} backed with a {@link HashSet}.
 *
 * @author datafox
 */
public class HashHandleSet extends HashSet<Handle> implements HandleSet {
    private final Logger logger;

    /**
     * {@link Space} associated with this set.
     */
    @Getter
    private final Space space;
    private final Map<String,Handle> ids;

    /**
     * Public constructor for {@link HashHandleSet}.
     *
     * @param space {@link Space} to be associated with this set
     * @param logger {@link Logger} for this set
     */
    public HashHandleSet(Space space, Logger logger) {
        super();
        this.logger = logger;
        this.space = space;
        ids = new HashMap<>();
    }

    /**
     * Public constructor for {@link HashHandleSet}. Uses {@link LoggerFactory#getLogger(Class)} with
     * {@link HashHandleSet HashHandleSet.class}.
     *
     * @param space {@link Space} to be associated with this set
     */
    public HashHandleSet(Space space) {
        this(space, LoggerFactory.getLogger(HashHandleSet.class));
    }

    /**
     * Returns the {@link Handle} in this set with the specified {@link String} id, or {@code null} if none is present.
     *
     * @param id {@link String} id of a {@link Handle}
     * @return {@link Handle} with the given id, or {@code null} if none is present
     * @throws NullPointerException  if the specified id is {@code null}
     */
    @Override
    public Handle get(String id) {
        checkNull(id, logger);
        return ids.get(id);
    }

    /**
     * Creates a {@link Handle} with the specified id if one does not already exist and adds it to this set.
     *
     * @param id id for the new {@link Handle}
     * @return created or pre-existing {@link Handle}
     * @throws UnsupportedOperationException if the {@code add} operation is not supported by this set
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or
     * more than one colon ({@code :})
     */
    @Override
    public Handle add(String id) {
        Handle handle = space.getOrCreateHandle(id);
        add(handle);
        return handle;
    }

    /**
     * Returns an unmodifiable version of this set. All changes made to the original set will be reflected in the
     * returned one.
     *
     * @return unmodifiable version of this set
     */
    @Override
    public HandleSet unmodifiable() {
        return new UnmodifiableHandleSet(this, logger);
    }

    /**
     * Returns all {@link Handle Handles} in this set containing the specified tag. The tag may be a {@link Handle} or
     * its {@link String} id.
     *
     * @param tag tag {@link Handle} or its {@link String} id
     * @return all {@link Handle Handles} in this set containing the specified tag
     * @throws ClassCastException if the tag is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the tag is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} is not a tag
     */
    @Override
    public Collection<Handle> getByTag(Object tag) {
        checkNullAndType(tag, logger);
        checkTag(tag, logger);
        return stream()
                .filter(h -> h.getTags().contains(tag))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all {@link Handle Handles} in this set containing the specified tags. The tags may be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param tags tag {@link Handle Handles} or their {@link String} ids
     * @return all {@link Handle Handles} in this set containing the specified tags
     * @throws ClassCastException if any of the tags is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified collection contains one or more {@code null} elements or if the
     * collection itself is {@code null}
     * @throws IllegalArgumentException if any of the {@link Handle Handles} is not a tag
     */
    @Override
    public Collection<Handle> getByTags(Collection<?> tags) {
        tags.forEach(handle -> checkNullAndType(handle, logger));
        tags.forEach(handle -> checkTag(handle, logger));
        return stream()
                .filter(h -> h.getTags().containsAll(tags))
                .collect(Collectors.toSet());
    }

    /**
     * Returns {@code true} if this set contains the specified element. The element may be a {@link Handle} or its
     * {@link String} id.
     *
     * @param o {@link Handle} element or its {@link String} id whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the object is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified element is {@code null}
     */
    @Override
    public boolean contains(Object o) {
        checkNullAndType(o, logger);
        return containsInternal(o);
    }

    /**
     * Adds the specified element to this set if it is not already present. The {@link Handle} element must be present
     * in this set's associated {@link Space}.
     *
     * @param handle element to be added to this set
     * @return {@code true} if this set did not already contain the specified element
     * @throws UnsupportedOperationException if the {@code add} operation is not supported by this set
     * @throws NullPointerException if the specified element is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} element is not present in this set's associated
     * {@link Space}
     */
    @Override
    public boolean add(Handle handle) {
        checkNullAndSpace(handle, space, logger);
        return addInternal(handle);
    }

    /**
     * Removes the specified element from this set if it is present. The element may be a {@link Handle} or its
     * {@link String} id.
     *
     * @param o {@link Handle} element or its {@link String} id to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this set
     * @throws ClassCastException if the object is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified element is {@code null}
     */
    @Override
    public boolean remove(Object o) {
        checkNullAndType(o, logger);
        return removeInternal(o);
    }

    /**
     * Returns {@code true} if this set contains all elements present in the specified collection. The elements may be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param c {@link Handle} elements or their {@link String} ids to be checked for containment in this set
     * @return {@code true} if this set contains all elements of the specified collection
     * @throws ClassCastException if any of the elements is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified collection contains one or more {@code null} elements
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        c.forEach(handle -> checkNullAndType(handle, logger));
        return c.stream().allMatch(this::containsInternal);
    }

    /**
     * Adds all elements in the specified collection to this set if they're not already present. The {@link Handle}
     * elements must be present in this set's associated {@link Space}.
     *
     * @param c collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is not supported by this set
     * @throws NullPointerException if the specified collection contains one or more {@code null} elements
     * @throws IllegalArgumentException if any of the {@link Handle} elements is not present within this set's
     * associated {@link Space}
     */
    @Override
    public boolean addAll(Collection<? extends Handle> c) {
        c.forEach(handle -> checkNullAndSpace(handle, space, logger));
        return c.stream()
                .map(this::addInternal)
                .reduce(false, Boolean::logicalOr);
    }

    /**
     * Retains only the elements in this set that are contained in the specified collection. The elements may be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param c collection containing elements to be retained in this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is not supported by this set
     * @throws ClassCastException if any of the elements is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified collection contains a {@code null} element
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        c.forEach(handle -> checkNullAndType(handle, logger));
        return new HashSet<>(this)
                .stream()
                .filter(handle -> !c.contains(handle) && !c.contains(handle.getId()))
                .map(this::removeInternal)
                .reduce(false, Boolean::logicalOr);
    }

    /**
     * Removes from this set all of its elements that are contained in the specified collection. The elements may be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param c collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation is not supported by this set
     * @throws ClassCastException if any of the elements is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified collection contains a {@code null} element
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(handle -> checkNullAndType(handle, logger));
        return c.stream()
                .map(this::removeInternal)
                .reduce(false, Boolean::logicalOr);
    }

    private boolean containsInternal(Object o) {
        if(o instanceof String) {
            return ids.containsKey(o);
        }
        return super.contains(o);
    }

    private boolean addInternal(Handle handle) {
        ids.put(handle.getId(), handle);
        return super.add(handle);
    }

    private boolean removeInternal(Object o) {
        Handle handle = null;
        if(o instanceof String) {
            if(ids.containsKey(o)) {
                handle = ids.get(o);
            }
        } else {
            handle = (Handle) o;
        }
        if(handle == null) {
            return false;
        }
        ids.remove(handle.getId());
        return super.remove(handle);
    }
}
