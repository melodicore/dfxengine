package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.api.TextProcessor;

/**
 * @author datafox
 */
public abstract class AbstractTextDefinition implements TextDefinition {
    protected abstract String getTextInternal(TextFactory factory, TextContext context);

    protected String processText(String str, TextFactory factory, TextContext context) {
        for(TextProcessor processor : factory.getTextProcessors()) {
            str = processor.process(str, factory, context);
        }
        return str;
    }

    @Override
    public String getText(TextFactory factory, TextContext context) {
        return processText(getTextInternal(factory, context), factory, context);
    }
}
