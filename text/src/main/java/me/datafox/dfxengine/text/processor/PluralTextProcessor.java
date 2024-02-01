package me.datafox.dfxengine.text.processor;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.api.TextProcessor;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;

/**
 * @author datafox
 */
@Component
public class PluralTextProcessor implements TextProcessor {
    @Override
    public String process(String str, TextFactory factory, TextContext context) {
        if(context.get(PLURAL_TEXT_PROCESSOR_USE) && !context.get(SINGULAR)) {
            return factory.getPluralConverter().apply(str);
        }
        return str;
    }

    @Override
    public int priority() {
        return PLURAL_TEXT_PROCESSOR_PRIORITY;
    }
}
