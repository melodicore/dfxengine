package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
@FunctionalInterface
public interface Text {
    String get(TextFactory factory, TextConfiguration configuration);
}
