package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

/**
 * @author datafox
 */
public class SimpleTextDefinition implements TextDefinition {
    private final String text;

    public SimpleTextDefinition(String text) {
        this.text = text;
    }

    @Override
    public String getText(TextFactory factory, TextContext context) {
        return text;
    }
}
