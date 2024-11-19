package me.datafox.dfxengine.text.api;

/**
 * An interface that signals that the implementing class has a name in singular and plural form. {@link T} must be the
 * class of the implementing class or its superclass or superinterface. Classes implementing this interface will have
 * priority over {@link NameConverter NameConverters}, but not the names explicitly configured with the
 * {@link TextFactory}.
 *
 * @author datafox
 */
public interface Named<T> {
    /**
     * Returns the name of this object.
     *
     * @return {@link Name} of this object
     */
    Name<T> getName();
}
