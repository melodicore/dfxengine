package me.datafox.dfxengine.text.utils.internal;

import me.datafox.dfxengine.text.api.NameConverter;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.NumberSuffixFormatter;

import java.math.BigDecimal;

/**
 * All string literals used for logging in the Text module.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class TextStrings {
    public static final String INVALID_NUMBER_FORMATTER =
            "Invalid NumberFormatter configuration, using toString() on the number directly";
    public static final String SPNF_DELEGATE_IS_SPLITTING =
            "SplittingNumberFormatter's delegate formatter cannot be a SplittingNumberFormatter";
    public static final String SPNF_SPLITS_NOT_IN_ORDER =
            "SplittingNumberFormatter's configured split multipliers are not in ascending order";
    public static final String CDSF_EMPTY_CHAR_ARRAY = "CharDigitSuffixFormatter's character array must not be empty";
    public static final String CDSF_NOT_DISTINCT_CHAR_ARRAY =
            "CharDigitSuffixFormatter's character array has the same element multiple times";

    private static final String ELNF_INVALID_LENGTH =
            "EvenLengthNumberFormatter's length is configured to be %s but needs to be positive and non-zero";
    private static final String ELNF_INVALID_MIN_EXPONENT =
            "EvenLengthNumberFormatter's minimum exponent is configured to be %s but needs to be positive";
    private static final String ELNF_LENGTH_MIN_EXPONENT_MISMATCH = "EvenLengthNumberFormatter's length is configured " +
            "to be %s and minimum exponent %s, but length needs to smaller than or equal to the minimum exponent";
    private static final String ELNF_TOO_LONG_NUMBER = "EvenLengthNumberFormatter was given " +
            "the number %s but it cannot be formatted to be equal to configured length %s";
    private static final String SNF_INVALID_PRECISION =
            "SimpleNumberFormatter's precision is configured to be %s but needs to be positive and non-zero";
    private static final String SNF_INVALID_MIN_EXPONENT =
            "SimpleNumberFormatter's minimum exponent is configured to be %s but needs to be positive";
    private static final String SNF_PRECISION_MIN_EXPONENT_MISMATCH = "SimpleNumberFormatter's precision is configured " +
            "to be %s and minimum exponent %s, but precision needs to smaller than or equal to the minimum exponent";
    private static final String SPNF_NEGATIVE_NUMBER = "SplittingNumberFormatter was " +
            "given the number %s but it is negative, using configured NumberFormatter as is";
    private static final String CDSF_INVALID_INTERVAL =
            "CharDigitSuffixFormatter's interval is configured to be %s but needs to be positive and non-zero";
    private static final String ESF_INVALID_INTERVAL =
            "ExponentSuffixFormatter's interval is configured to be %s but needs to be positive and non-zero";
    private static final String NSF_INVALID_INTERVAL =
            "NamedSuffixFormatter's interval is configured to be %s but needs to be positive and non-zero";
    private static final String CONVERTER_PRESENT = "NameConverter for class %s is already present, %s will be ignored";
    private static final String FORMATTER_PRESENT =
            "NumberFormatter with handle %s is already present, %s will be ignored";
    private static final String SUFFIX_FORMATTER_PRESENT =
            "NumberSuffixFormatter with handle %s is already present, %s will be ignored";
    private static final String NOT_INFINITE =
            "NumberSuffixFormatter %s is not infinite and cannot be used as a default";

    public static String elnfInvalidLength(int length) {
        return String.format(ELNF_INVALID_LENGTH, length);
    }

    public static String elnfInvalidMinExponent(int minExponent) {
        return String.format(ELNF_INVALID_MIN_EXPONENT, minExponent);
    }

    public static String elnfLengthMinExponentMismatch(int length, int minExponent) {
        return String.format(ELNF_LENGTH_MIN_EXPONENT_MISMATCH, length, minExponent);
    }

    public static String elnfTooLongNumber(BigDecimal original, int length) {
        return String.format(ELNF_TOO_LONG_NUMBER, original, length);
    }

    public static String snfInvalidPrecision(int precision) {
        return String.format(SNF_INVALID_PRECISION, precision);
    }

    public static String snfInvalidMinExponent(int minExponent) {
        return String.format(SNF_INVALID_MIN_EXPONENT, minExponent);
    }

    public static String snfPrecisionMinExponentMismatch(int precision, int minExponent) {
        return String.format(SNF_PRECISION_MIN_EXPONENT_MISMATCH, precision, minExponent);
    }

    public static String spnfNegativeNumber(BigDecimal number) {
        return String.format(SPNF_NEGATIVE_NUMBER, number);
    }

    public static String cdsfInvalidInterval(int interval) {
        return String.format(CDSF_INVALID_INTERVAL, interval);
    }

    public static String esfInvalidInterval(int interval) {
        return String.format(ESF_INVALID_INTERVAL, interval);
    }

    public static String nsfInvalidInterval(int interval) {
        return String.format(NSF_INVALID_INTERVAL, interval);
    }

    public static String converterPresent(NameConverter<?> converter) {
        return String.format(CONVERTER_PRESENT, converter.getType(), converter);
    }

    public static String formatterPresent(NumberFormatter formatter) {
        return String.format(FORMATTER_PRESENT, formatter.getHandle(), formatter);
    }

    public static String suffixFormatterPresent(NumberSuffixFormatter formatter) {
        return String.format(SUFFIX_FORMATTER_PRESENT, formatter.getHandle(), formatter);
    }

    public static String notInfinite(NumberSuffixFormatter formatter) {
        return String.format(NOT_INFINITE, formatter);
    }
}
