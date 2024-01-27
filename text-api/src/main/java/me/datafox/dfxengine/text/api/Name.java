package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface Name<T> {
    T getOwner();

    String getSingular();

    String getPlural();
}
