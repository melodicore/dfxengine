package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.text.api.Formatter;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author datafox
 */
public class EvenLengthNumberFormatter implements Formatter<Number> {
    private final MathContext context;
    private final boolean engineering;

    public EvenLengthNumberFormatter(int characters, boolean engineering) {
        if(characters <= (engineering ? 5 : 3)) {
            throw new IllegalArgumentException("bad character amount");
        }

        context = new MathContext(characters);
        this.engineering = engineering;
    }

    @Override
    public String format(Number number) {
        BigDecimal bd = NumberUtils
                .toBigDecimal(number)
                .round(context);

        String str;

        if(engineering) {
            str = bd.toEngineeringString();
        } else {
            str = bd.toString();
        }

        if(str.length() == context.getPrecision()) {
            return str;
        }

        if(str.length() < context.getPrecision()) {
            int missing = context.getPrecision() - str.length() - 1;
            str += ".";
            if(missing > 0) {
                str += "0".repeat(missing);
            }
            return str;
        }

        if(str.contains("E")) {
            int index = str.indexOf('E');
            int exponentLength = str.length() - index - 1;
            return str.substring(0, index - 2 - exponentLength) + str.substring(index);
        }

        return str.substring(0, str.length() - 1);
    }

    public enum Type {
        SCIENTIFIC, ENGINEERING
    }
}
