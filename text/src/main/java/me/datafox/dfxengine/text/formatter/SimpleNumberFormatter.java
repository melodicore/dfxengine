package me.datafox.dfxengine.text.formatter;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.TextHandles;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author datafox
 */
@Component
public class SimpleNumberFormatter implements NumberFormatter {
    /**
     * Precision, equivalent to {@link MathContext#getPrecision()}. The default value is {@code 6}.
     */
    public static final ConfigurationKey<Integer> PRECISION = ConfigurationKey.of(6);

    /**
     * Minimum absolute exponent to be formatted with a suffix. It must be smaller than or equal to {@link #PRECISION}.
     * The default value is {@code 3}.
     */
    public static final ConfigurationKey<Integer> MIN_EXPONENT = ConfigurationKey.of(3);

    @Getter
    private final Handle handle;

    @Inject
    public SimpleNumberFormatter(TextHandles handles) {
        handle = handles.getSimpleNumberFormatter();
    }

    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int precision = configuration.get(PRECISION);
        int minExponent = configuration.get(MIN_EXPONENT);
        NumberSuffixFactory suffixFactory = factory.getNumberSuffixFactory(configuration);
        if(suffixFactory == null) {
            suffixFactory = factory.getDefaultNumberSuffixFactory();
        }
        NumberSuffixFactory.Output output = suffixFactory.format(number, factory, configuration);
        String suffix = "";
        if(Math.abs(output.getExponent()) >= minExponent) {
            number = output.getScaled();
            suffix = output.getSuffix();
        }
        return number.round(new MathContext(precision)).toPlainString() + suffix;
    }
}
