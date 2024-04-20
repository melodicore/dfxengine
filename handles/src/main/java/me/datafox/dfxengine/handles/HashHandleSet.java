package me.datafox.dfxengine.handles;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.utils.UnmodifiableHandleSet;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.utils.HandleUtils.checkNullAndSpace;
import static me.datafox.dfxengine.handles.utils.HandleUtils.checkNullAndType;

/**
 * An unordered implementation of {@link HandleSet} backed with a {@link HashSet}.
 *
 * @author datafox
 */
public class HashHandleSet extends HashSet<Handle> implements HandleSet {
    private final Logger logger;
    @Getter
    private final Space space;
    private final Map<String,Handle> ids;

    public HashHandleSet(Space space, Logger logger) {
        super();
        this.logger = logger;
        this.space = space;
        ids = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public Handle get(String id) {
        return ids.get(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Handle add(String id) {
        Handle handle = space.getOrCreateHandle(id);
        add(handle);
        return handle;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleSet unmodifiable() {
        return new UnmodifiableHandleSet(this, logger);
    }

    /**
     * {@inheritDoc}
     *
     * @param tag {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Collection<Handle> getByTag(Object tag) {
        checkNullAndType(tag, logger);
        return stream()
                .filter(h -> h.getTags().contains(tag))
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     *
     * @param tags {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Collection<Handle> getByTags(Collection<?> tags) {
        tags.forEach(handle -> checkNullAndType(handle, logger));
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
     * @throws ClassCastException if the type of the specified element is incompatible with this set
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
     * @throws ClassCastException if the type of the specified element is incompatible with this set
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
     * @throws ClassCastException if the type of the specified element is incompatible with this set
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
     * @throws ClassCastException if the type of a specified element is incompatible with this set
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
     * @throws ClassCastException if the type of a specified element is incompatible with this set
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
     * @throws ClassCastException if the type of an element in the specified collection is incompatible with this set
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
     * @throws ClassCastException if the type of an element in the specified collection is incompatible with this set
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
