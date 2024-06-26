package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
@FunctionalInterface
public interface PluralConverter {
    String convert(String singular);
}
