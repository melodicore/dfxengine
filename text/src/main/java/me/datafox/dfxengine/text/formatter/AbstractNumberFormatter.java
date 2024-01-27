package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextFactory;
import org.slf4j.Logger;

/**
 * @author datafox
 */
public abstract class AbstractNumberFormatter implements NumberFormatter {
    protected final Logger logger;
    private final Handle handle;

    protected AbstractNumberFormatter(Logger logger, TextFactory textFactory, String handleId) {
        this.logger = logger;
        this.handle = textFactory.getNumberFormatterSpace().getOrCreateHandle(handleId);
    }

    @Override
    public Handle getHandle() {
        return handle;
    }
}
