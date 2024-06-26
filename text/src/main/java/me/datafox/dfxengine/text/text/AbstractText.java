package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public abstract class AbstractText implements Text {
    protected final Logger logger;
    protected final TextConfiguration configuration;

    protected AbstractText(TextConfiguration configuration) {
        logger = LoggerFactory.getLogger(getClass());
        this.configuration = configuration;
    }

    @Override
    public String get(TextFactory factory, TextConfiguration configuration) {
        if(this.configuration != null) {
            configuration = configuration.copy();
            configuration.set(this.configuration);
        }
        return generate(factory, configuration);
    }

    protected abstract String generate(TextFactory factory, TextConfiguration configuration);
}
