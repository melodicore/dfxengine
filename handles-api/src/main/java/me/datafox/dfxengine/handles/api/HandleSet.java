package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.Set;

/**
 * An extension of {@link Set} that may only contain {@link Handle Handles} from a specified {@link Space}. Also has
 * extra functionality to query elements based on their {@link String} id.
 *
 * @author datafox
 */
public interface HandleSet extends Set<Handle> {
    /**
     * Returns the {@link Space} associated with this set. All {@link Handle Handles} present in this set must be
     * associated with this space.
     *
     * @return {@link Space} associated with this set
     */
    Space getSpace();

    /**
     * Returns the {@link Handle} in this set with the specified {@link String} id, or {@code null} if none is present.
     *
     * @param id {@link String} id of a {@link Handle}
     * @return {@link Handle} with the given id, or {@code null} if none is present
     * @throws NullPointerException  if the specified {@link Handle} is {@code null}
     */
    Handle get(String id);

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
    Handle add(String id);

    /**
     * Returns an unmodifiable version of this set. All changes made to the original set will be reflected in the
     * returned one.
     *
     * @return unmodifiable version of this set
     */
    HandleSet unmodifiable();

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
    Collection<Handle> getByTag(Object tag);

    /**
     * Returns all {@link Handle Handles} in this set containing the specified tags. The tags may be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param tags tag {@link Handle Handles} or their {@link String} ids
     * @return all {@link Handle Handles} in this set containing the specified tags
     * @throws ClassCastException if any of the tags are not {@link Handle Handles} or a {@link String Strings}
     * @throws NullPointerException if any of the tags is {@code null}
     * @throws IllegalArgumentException if any of the {@link Handle Handles} is not a tag
     */
    Collection<Handle> getByTags(Collection<?> tags);

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
    boolean contains(Object o);

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
    boolean add(Handle handle);

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
    boolean remove(Object o);

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
    boolean containsAll(Collection<?> c);

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
    boolean addAll(Collection<? extends Handle> c);

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
    boolean retainAll(Collection<?> c);

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
    boolean removeAll(Collection<?> c);
}
