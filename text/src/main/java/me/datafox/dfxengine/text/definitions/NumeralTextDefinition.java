package me.datafox.dfxengine.text.definitions;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
public class NumeralTextDefinition extends NumberTextDefinition {
    public NumeralTextDefinition(Numeral numeral, String numberFormatterId) {
        super(numeral.getNumber(), numberFormatterId);
    }

    public NumeralTextDefinition(Numeral number, Handle numberFormatterHandle) {
        super(number.getNumber(), numberFormatterHandle);
    }

    public NumeralTextDefinition(Numeral number) {
        super(number.getNumber());
    }
}
