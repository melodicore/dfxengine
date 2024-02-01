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
public class BasicNumberFormatter extends AbstractNumberFormatter {
    @Inject
    private BasicNumberFormatter(Logger logger, TextFactory textFactory) {
        super(logger, textFactory, BASIC_NUMBER_FORMATTER_HANDLE_ID);
    }

    @Override
    public Details format(Number number, TextContext context) {
        boolean scientific = context.get(NUMBER_FORMATTER_SCIENTIFIC);
        boolean mantissaPlus = context.get(NUMBER_FORMATTER_USE_MANTISSA_PLUS);
        int precision = context.get(BASIC_NUMBER_FORMATTER_PRECISION);

        if(precision <= 0) {
            LogUtils.logExceptionAndGet(logger,
                    "precision must be a non-zero positive integer",
                    IllegalArgumentException::new);
        }

        BigDecimal bd = NumberUtils
                .toBigDecimal(number)
                .round(new MathContext(precision));

        Details.DetailsBuilder builder = Details
                .builder()
                .number(number)
                .one(bd.compareTo(BigDecimal.ONE) == 0);

        String str;

        if(scientific) {
            str = bd.toString();
        } else {
            str = bd.toEngineeringString();
        }

        if(!mantissaPlus && str.contains("+")) {
            str = str.replace("+", "");
        }

        return builder
                .string(str)
                .build();
    }
}
