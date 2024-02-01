package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.BASIC_NUMBER_FORMATTER_HANDLE_ID;
import static me.datafox.dfxengine.text.utils.TextFactoryConstants.SINGULAR;

/**
 * @author datafox
 */
public class NumberTextDefinition extends AbstractTextDefinition {
    private final Number number;
    private final String numberFormatterId;

    public NumberTextDefinition(Number number, String numberFormatterId) {
        this.number = number;
        this.numberFormatterId = numberFormatterId;
    }

    public NumberTextDefinition(Number number, Handle numberFormatterHandle) {
        this(number, numberFormatterHandle.getId());
    }

    public NumberTextDefinition(Number number) {
        this(number, BASIC_NUMBER_FORMATTER_HANDLE_ID);
    }

    @Override
    protected String getTextInternal(TextFactory factory, TextContext context) {
        NumberFormatter.Details details = factory
                .getNumberFormatterById(numberFormatterId)
                .orElseThrow()
                .format(number, context);
        context.set(SINGULAR, details.isOne());
        return details.getString();
    }
}
