package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.text.api.Formatter;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author datafox
 */
public class BasicNumberFormatter implements Formatter<Number> {
    private final MathContext context;
    private final boolean engineering;

    public BasicNumberFormatter(int digits, boolean engineering) {
        if(digits <= 0) {
            throw new IllegalArgumentException("bad digit amount");
        }

        context = new MathContext(digits);
        this.engineering = engineering;
    }

    @Override
    public String format(Number number) {
        BigDecimal bd = NumberUtils
                .toBigDecimal(number)
                .round(context);

        if(engineering) {
            return bd.toEngineeringString();
        } else {
            return bd.toString();
        }
    }

    public enum Type {
        SCIENTIFIC, ENGINEERING
    }
}
