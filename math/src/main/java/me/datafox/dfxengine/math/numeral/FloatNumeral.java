package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implementation of {@link Numeral} backed with a {@code float}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class FloatNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(FloatNumeral.class);

    private final float number;

    /**
     * @param number {@code float} to be associated with this numeral
     *
     * @throws IllegalArgumentException if the {@code float} is {@code NaN} or infinite.
     */
    public FloatNumeral(float number) {
        super(NumeralType.FLOAT);

        if(Double.isNaN(number)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.nanFloatValue(),
                    IllegalArgumentException::new);
        }

        if(Float.isInfinite(number)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.infiniteFloatValue(),
                    IllegalArgumentException::new);
        }

        this.number = number;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@code float} of this numeral
     */
    @Override
    public float floatValue() {
        return number;
    }

    /**
     * Conversion from {@code float} to {@link BigInteger} is done using
     * {@link #bigDecValue()}{@link BigDecimal#toBigInteger() .toBigInteger()}, which uses the {@link String}
     * representation to round inaccuracies.
     *
     * @return the value of this numeral as a {@link BigInteger}
     */
    @Override
    public BigInteger bigIntValue() {
        return bigDecValue().toBigInteger();
    }

    /**
     * Conversion from {@code float} to {@code double} is done using the {@link String} representation to round
     * inaccuracies.
     *
     * @return the value of this numeral as a {@code double}
     */
    @Override
    public double doubleValue() {
        return Double.parseDouble(Float.toString(number));
    }

    /**
     * Conversion from {@code float} to {@link BigDecimal} is done using the {@link String} representation to round
     * inaccuracies.
     *
     * @return the value of this numeral as a {@link BigDecimal}
     */
    @Override
    public BigDecimal bigDecValue() {
        return new BigDecimal(Float.toString(number));
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
