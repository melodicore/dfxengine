package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.values.api.Value;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;

/**
 * @author datafox
 */
public class ValueTextDefinition extends AbstractTextDefinition implements Dependency {
    private final Value value;
    private final String numberFormatterId;
    private Numeral cachedBase;
    private Numeral cachedValue;

    public ValueTextDefinition(Value value, String numberFormatterId) {
        this.value = value;
        this.numberFormatterId = numberFormatterId;
        cachedBase = null;
        cachedValue = null;

        value.addDependency(this);
    }

    public ValueTextDefinition(Value value, Handle numberFormatterHandle) {
        this(value, numberFormatterHandle.getId());
    }

    public ValueTextDefinition(Value value) {
        this(value, BASIC_NUMBER_FORMATTER_HANDLE_ID);
    }

    @Override
    protected String getTextInternal(TextFactory factory, TextContext context) {
        Number number;
        if(context.get(USE_VALUE_BASE)) {
            if(cachedBase == null) {
                cachedBase = value.getBase();
            }
            number = cachedBase.getNumber();
        } else {
            if(cachedValue == null) {
                cachedValue = value.getValue();
            }
            number = cachedValue.getNumber();
        }
        NumberFormatter.Details details = factory
                .getNumberFormatterById(numberFormatterId)
                .orElseThrow()
                .format(number, context);
        context.set(SINGULAR, details.isOne());
        return details.getString();
    }

    @Override
    public void invalidate() {
        cachedBase = null;
        cachedValue = null;
    }
}
