package me.datafox.dfxengine.text.processor;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.api.TextProcessor;
import me.datafox.dfxengine.utils.StringUtils;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;

/**
 * @author datafox
 */
@Component
public class CaseTextProcessor implements TextProcessor {
    @Override
    public String process(String str, TextFactory factory, TextContext context) {
        if(context.get(CASE_TEXT_PROCESSOR_UPPERCASE)) {
            str = str.toUpperCase();
        }
        if(context.get(CASE_TEXT_PROCESSOR_LOWERCASE)) {
            str = str.toLowerCase();
        }
        if(context.get(CASE_TEXT_PROCESSOR_CAPITALIZE)) {
            str = StringUtils.capitalize(str);
        }
        return str;
    }

    @Override
    public int priority() {
        return CASE_TEXT_PROCESSOR_PRIORITY;
    }
}
