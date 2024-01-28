package me.datafox.dfxengine.text.utils;

import me.datafox.dfxengine.text.api.TextContextConverter;
import me.datafox.dfxengine.text.api.TextContextData;
import me.datafox.dfxengine.text.converter.BooleanTextContextConverter;
import me.datafox.dfxengine.text.converter.IntegerTextContextConverter;
import me.datafox.dfxengine.text.converter.StringTextContextConverter;

/**
 * @author datafox
 */
public class TextFactoryConstants {
    public static final String TEXT_FACTORY_NUMBER_FORMATTER_SPACE_ID = "textFactoryNumberFormatters";

    public static final String BASIC_NUMBER_FORMATTER_HANDLE_ID = "basic";

    public static final String EVEN_LENGTH_NUMBER_FORMATTER_HANDLE_ID = "evenLength";

    public static final TextContextConverter<String> STRING_CONVERTER = new StringTextContextConverter();

    public static final TextContextConverter<Boolean> BOOLEAN_CONVERTER = new BooleanTextContextConverter();

    public static final TextContextConverter<Integer> INTEGER_CONVERTER = new IntegerTextContextConverter();

    public static final TextContextData<Boolean> SINGULAR =
            new TextContextData<>("singular", true, BOOLEAN_CONVERTER);

    public static final TextContextData<String> DELIMITER =
            new TextContextData<>("delimiter", " ", STRING_CONVERTER);

    public static final TextContextData<Boolean> USE_VALUE_BASE =
            new TextContextData<>("useValueBase", false, BOOLEAN_CONVERTER);

    public static final TextContextData<Boolean> NUMBER_FORMATTER_SCIENTIFIC =
            new TextContextData<>("numberFormatterScientific", true, BOOLEAN_CONVERTER);

    public static final TextContextData<Integer> BASIC_NUMBER_FORMATTER_PRECISION =
            new TextContextData<>("basicNumberFormatterPrecision", 4, INTEGER_CONVERTER);

    public static final TextContextData<Integer> EVEN_LENGTH_NUMBER_FORMATTER_CHARACTERS =
            new TextContextData<>("evenLengthNumberFormatterCharacters", 7, INTEGER_CONVERTER);

    public static final TextContextData<Boolean> EVEN_LENGTH_NUMBER_FORMATTER_USE_MANTISSA_PLUS =
            new TextContextData<>("evenLengthNumberFormatterUseMantissaPlus", false, BOOLEAN_CONVERTER);

}
