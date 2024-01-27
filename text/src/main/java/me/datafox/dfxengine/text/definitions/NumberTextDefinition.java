package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import static me.datafox.dfxengine.text.utils.internal.TextConstants.BASIC_NUMBER_FORMATTER_HANDLE_ID;
import static me.datafox.dfxengine.text.utils.internal.TextConstants.SINGULAR_HANDLE_ID;

/**
 * @author datafox
 */
public class NumberTextDefinition implements TextDefinition {
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
    public String getText(TextFactory factory, TextFactory.Context context) {
        NumberFormatter.Details details = factory
                .getNumberFormatterById(numberFormatterId)
                .orElseThrow()
                .format(number, context);
        context.setById(SINGULAR_HANDLE_ID, details.isOne());
        return details.getString();
    }
}
