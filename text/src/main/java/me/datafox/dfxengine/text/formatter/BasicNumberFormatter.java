package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;

import static me.datafox.dfxengine.text.utils.internal.TextConstants.*;

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
    public Details format(Number number, TextFactory.Context context) {
        int precision = context.getById(
                BASIC_NUMBER_FORMATTER_PRECISION_HANDLE_ID,
                BASIC_NUMBER_FORMATTER_PRECISION_DEFAULT);

        boolean scientific = context.getById(
                NUMBER_FORMATTER_SCIENTIFIC_HANDLE_ID,
                NUMBER_FORMATTER_SCIENTIFIC_DEFAULT);

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

        if(scientific) {
            builder.string(bd.toString());
        } else {
            builder.string(bd.toEngineeringString());
        }

        return builder.build();
    }
}
