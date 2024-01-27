package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.values.api.Value;

import static me.datafox.dfxengine.text.utils.internal.TextConstants.*;

/**
 * @author datafox
 */
public class ValueTextDefinition implements TextDefinition, Dependency {
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
    public String getText(TextFactory factory, TextFactory.Context context) {
        Number number;
        if(context.getById(VALUE_USE_BASE_HANDLE_ID, VALUE_USE_BASE_DEFAULT)) {
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
        context.setById(SINGULAR_HANDLE_ID, details.isOne());
        return details.getString();
    }


    @Override
    public void invalidate() {
        cachedBase = null;
        cachedValue = null;
    }
}
