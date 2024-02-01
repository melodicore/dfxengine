package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;

/**
 * @author datafox
 */
@Component
public class EvenLengthNumberFormatter extends AbstractNumberFormatter {
    @Inject
    public EvenLengthNumberFormatter(Logger logger, TextFactory textFactory) {
        super(logger, textFactory, EVEN_LENGTH_NUMBER_FORMATTER_HANDLE_ID);
    }

    @Override
    public Details format(Number number, TextContext context) {
        boolean scientific = context.get(NUMBER_FORMATTER_SCIENTIFIC);
        boolean mantissaPlus = context.get(NUMBER_FORMATTER_USE_MANTISSA_PLUS);
        int characters = context.get(EVEN_LENGTH_NUMBER_FORMATTER_CHARACTERS);

        if(characters <= 0) {
            LogUtils.logExceptionAndGet(logger,
                    "characters must be a non-zero positive integer",
                    IllegalArgumentException::new);
        }

        BigDecimal bd = NumberUtils
                .toBigDecimal(number);

        boolean transformToE = false;
        int digits = characters;

        if(bd.scale() != 0) {
            digits--;
        }

        if(bd.compareTo(BigDecimal.ZERO) < 0) {
            digits--;
        }

        if(bd.abs().compareTo(BigDecimal.ONE) >= 0) {
            int diff = bd.precision() - bd.scale();
            if(diff == characters) {
                digits++;
            } else if(diff > characters) {
                int mantissaLength = (int) Math.log10(diff - 1) + 2;
                digits -= mantissaLength;
                if(!mantissaPlus) {
                    digits++;
                }
                if(characters > (mantissaLength + (mantissaPlus ? 2 : 1))) {
                    digits--;
                }
            }
            if(bd.scale() == 0 && bd.precision() > characters) {
                digits--;
            }
        } else {
            int diff = bd.scale() - bd.precision();
            if(diff >= 3) {
                if(diff < 6) {
                    transformToE = true;
                }
                int mantissaLength = (int) Math.log10(diff + 1) + 2;
                digits -= mantissaLength;
                if(characters > (mantissaLength + 2)) {
                    digits--;
                }
            } else {
                digits -= diff + 1;
            }
        }

        if(digits <= 0) {
            digits = 1;
        }

        MathContext mc = new MathContext(digits);

        if(transformToE) {
            bd = bd.divide(new BigDecimal("1E+30"), mc);
        } else {
            bd = bd.round(mc);
        }

        String str;

        if(scientific) {
            str = bd.toString();
        } else {
            str = bd.toEngineeringString();
        }

        if(transformToE) {
            int i = str.indexOf('E') + 1;
            int mantissa = Integer.parseInt(str.substring(i));
            mantissa += 30;
            str = str.substring(0, i) + mantissa;
        }

        if(!mantissaPlus && str.contains("+")) {
            str = str.replace("+", "");
        }

        if(str.length() < characters) {
            int diff = characters - str.length();
            int i = str.indexOf('E');
            String suffix = "";
            if(i >= 0) {
                suffix = str.substring(i);
                str = str.substring(0, i);
            }
            if(!str.contains(".")) {
                str += ".";
                diff--;
            }
            if(diff > 0) {
                str += "0".repeat(diff);
            }
            str += suffix;
        }

        if(str.length() > characters) {
            logger.warn("Number will not have the correct amount of characters due to too long mantissa, " +
                    "consider increasing character count");
        }

        return Details.builder()
                .number(number)
                .string(str)
                .one(str.matches("(1\\.0*)|1"))
                .build();
    }
}
