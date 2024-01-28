package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
@FunctionalInterface
public interface TextDefinition {
    String getText(TextFactory factory, TextContext context);
}
