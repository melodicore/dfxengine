package me.datafox.dfxengine.math.utils.internal;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class MathStrings {
    public static final String NULL_NUMBER_TYPE = "Encountered a null NumberType where one should not be present";
    public static final String EMPTY_ARRAY = "Encountered an empty array where a populated one should be present";

    private static final String COULD_NOT_CONVERT = "Could not convert %s to type %s";
    private static final String INFINITE_VALUE = "Attempted to instantiate a %sNumeral with an infinite %s value";
    private static final String NAN_VALUE = "Attempted to instantiate a %sNumeral with a NaN %s value";
    private static final String UNKNOWN_NUMBER_TYPE =
            "Number %s is not any of the recognised types Integer, Long, BigInteger, Float, Double and BigDecimal";
    private static final String UNKNOWN_TYPE = "Unknown NumeralType %s";
    private static final String DURING_CONVERSION = "Error happened while converting %s to %s";
    private static final String OVERFLOW = "%s overflow or underflow with %s %s";
    private static final String DIVISION_BY_ZERO = "Attempted to divide the number %s by zero";
    private static final String SQUARE_ROOT_OF_NEGATIVE =
            "Attempted to calculate the square root of the negative number %s";
    private static final String ROOT_BASE_ZERO = "Attempted to calculate the root of the number %s with base 0";
    private static final String ROOT_OF_NEGATIVE_EVEN_BASE =
            "Attempted to calculate the root of the negative number %s with the positive and even base %s";
    private static final String LOGARITHM_OF_ZERO_OR_NEGATIVE =
            "Attempted to calculate the logarithm of the zero or negative number %s";
    private static final String LOGARITHM_OF_BASE_ZERO_OR_NEGATIVE =
            "Attempted to calculate the logarithm of the number %s with the zero or negative base %s";
    private static final String LOGARITHM_OF_BASE_ONE =
            "Attempted to calculate the logarithm of the number %s with base 1";

    public static String nanDoubleValue() {
        return String.format(NAN_VALUE, "Double", "double");
    }

    public static String infiniteDoubleValue() {
        return String.format(INFINITE_VALUE, "Double", "double");
    }

    public static String nanFloatValue() {
        return String.format(NAN_VALUE, "Float", "float");
    }

    public static String infiniteFloatValue() {
        return String.format(INFINITE_VALUE, "Float", "float");
    }

    public static String unknownNumberType(Number number) {
        return String.format(UNKNOWN_NUMBER_TYPE, number);
    }

    public static String unknownType(NumeralType type) {
        return String.format(UNKNOWN_TYPE, type.name());
    }

    public static String duringIntConversion(Numeral numeral) {
        return String.format(DURING_CONVERSION, numeral, "int");
    }

    public static String duringLongConversion(Numeral numeral) {
        return String.format(DURING_CONVERSION, numeral, "long");
    }

    public static String duringFloatConversion(Numeral numeral) {
        return String.format(DURING_CONVERSION, numeral, "float");
    }

    public static String duringDoubleConversion(Numeral numeral) {
        return String.format(DURING_CONVERSION, numeral, "double");
    }

    public static String intOverflow(Number number) {
        String simpleName = number.getClass().getSimpleName();
        return String.format(OVERFLOW, "Integer",
                simpleName.startsWith("B") ? simpleName : simpleName.toLowerCase(), number);
    }

    public static String longOverflow(Number number) {
        String simpleName = number.getClass().getSimpleName();
        return String.format(OVERFLOW, "Long",
                simpleName.startsWith("B") ? simpleName : simpleName.toLowerCase(), number);
    }

    public static String floatOverflow(Number number) {
        String simpleName = number.getClass().getSimpleName();
        return String.format(OVERFLOW, "Float",
                simpleName.startsWith("B") ? simpleName : simpleName.toLowerCase(), number);
    }

    public static String doubleOverflow(Number number) {
        String simpleName = number.getClass().getSimpleName();
        return String.format(OVERFLOW, "Double",
                simpleName.startsWith("B") ? simpleName : simpleName.toLowerCase(), number);
    }

    public static String divisionByZero(Numeral numeral) {
        return String.format(DIVISION_BY_ZERO, numeral);
    }

    public static String squareRootOfNegative(Numeral numeral) {
        return String.format(SQUARE_ROOT_OF_NEGATIVE, numeral);
    }

    public static String rootBaseZero(Numeral numeral) {
        return String.format(ROOT_BASE_ZERO, numeral);
    }

    public static String rootOfNegativeEvenBase(Numeral numeral, Numeral base) {
        return String.format(ROOT_OF_NEGATIVE_EVEN_BASE, numeral, base);
    }

    public static String logarithmOfZeroOrNegative(Numeral numeral) {
        return String.format(LOGARITHM_OF_ZERO_OR_NEGATIVE, numeral);
    }

    public static String logarithmOfBaseZeroOrNegative(Numeral numeral, Numeral base) {
        return String.format(LOGARITHM_OF_BASE_ZERO_OR_NEGATIVE, numeral, base);
    }

    public static String logarithmOfBaseOne(Numeral numeral) {
        return String.format(LOGARITHM_OF_BASE_ONE, numeral);
    }

    public static String couldNotConvert(Numeral numeral, NumeralType type) {
        return String.format(COULD_NOT_CONVERT, numeral, type.name());
    }
}
