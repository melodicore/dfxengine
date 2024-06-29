package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.internal.TextStrings;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * A {@link Text} implementation that formats a supplied {@link Number} using a {@link NumberFormatter} determined by
 * {@link ConfigurationKeys#NUMBER_FORMATTER}.
 *
 * @author datafox
 */
public class NumberText extends AbstractText {
    private final Supplier<Number> supplier;

    /**
     * @param supplier {@link Supplier} for the {@link Number} to be used
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public NumberText(Supplier<Number> supplier, TextConfiguration configuration) {
        super(configuration);
        this.supplier = supplier;
    }

    /**
     * @param supplier {@link Supplier} for the {@link Number} to be used
     */
    public NumberText(Supplier<Number> supplier) {
        this(supplier, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        Number supplied = supplier.get();
        if(formatter == null) {
            logger.warn(TextStrings.INVALID_NUMBER_FORMATTER);
            return supplied.toString();
        }
        BigDecimal number;
        if(supplied instanceof BigDecimal) {
            number = (BigDecimal) supplied;
        } else {
            number = new BigDecimal(supplied.toString());
        }
        return formatter.format(number, factory, configuration);
    }
}
