package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;

/**
 * @author datafox
 */
public class SimpleTextDefinition extends AbstractTextDefinition {
    private final String text;

    public SimpleTextDefinition(String text) {
        this.text = text;
    }

    @Override
    protected String getTextInternal(TextFactory factory, TextContext context) {
        return text;
    }
}
