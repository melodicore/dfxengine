package me.datafox.dfxengine.text.suffix;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberSuffixFactory;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.TextHandles;

import java.math.BigDecimal;

/**
 * @author datafox
 */
@Component
public class NamedSuffixFactory implements NumberSuffixFactory {
    public static final String[] SI = new String[] {
            "", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
    public static final String[] SHORT = new String[] {
            "", "K", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "Oc", "No",
            "Dc", "UDc", "DDc", "TDc", "QaDc", "QiDc", "SxDc", "SpDc", "ODc", "NDc",
            "Vi", "UVi", "DVi", "TVi", "QaVi", "QiVi", "SxVi", "SpVi", "OVi", "NVi",
            "Tg", "UTg", "DTg", "TTg", "QaTg", "QiTg", "SxTg", "SpTg", "OTg", "NTg",
            "Qd", "UQd", "DQd", "TQd", "QaQd", "QiQd", "SxQd", "SpQd", "OQd", "NQd",
            "Qq", "UQq", "DQq", "TQq", "QaQq", "QiQq", "SxQq", "SpQq", "OQq", "NQq",
            "Sg", "USg", "DSg", "TSg", "QaSg", "QiSg", "SxSg", "SpSg", "OSg", "NSg",
            "St", "USt", "DSt", "TSt", "QaSt", "QiSt", "SxSt", "SpSt", "OSt", "NSt",
            "Og", "UOg", "DOg", "TOg", "QaOg", "QiOg", "SxOg", "SpOg", "OOg", "NOg" };
    public static final String[] LONG = new String[] {
            "", "K", "M", "Md", "B", "Bd", "T", "Td", "Qa", "Qad",
            "Qi", "Qid", "Sx", "Sxd", "Sp", "Spd", "Oc", "Od", "No", "Nd",
            "Dc", "Dd", "UDc", "UDd", "DDc", "DDd", "TDc", "TDd", "QaDc", "QaDd",
            "QiDc", "QiDd", "SxDc", "SxDd", "SpDc", "SpDd", "ODc", "ODd", "NDc", "NDd",
            "Vi", "Vd", "UVi", "UVd", "DVi", "DVd", "TVi", "TVd", "QaVi", "QaVd",
            "QiVi", "QiVd", "SxVi", "SxVd", "SpVi", "SpVd", "OVi", "OVd", "NVi", "NVd",
            "Tg", "TD", "UTg", "UTD", "DTg", "DTD", "TTg", "TTD", "QaTg", "QaTD",
            "QiTg", "QiTD", "SxTg", "SxTD", "SpTg", "SpTD", "OTg", "OTD", "NTg", "NTD",
            "Qd", "QD", "UQd", "UQD", "DQd", "DQD", "TQd", "TQD", "QaQd", "QaQD" };

    /**
     * Array of suffixes to be used. Every suffix has an exponent interval of {@code 3} and starts from exponent
     * {@code 0}, so the first element denotes {@code e0}, second {@code e3}, third {@code e6} and so on. The default
     * value is {@link #SHORT}.
     */
    public static final ConfigurationKey<String[]> SUFFIXES = ConfigurationKey.of(SHORT);

    @Getter
    private final Handle handle;

    @Inject
    public NamedSuffixFactory(TextHandles handles) {
        handle = handles.getNamedSuffixFactory();
    }

    @Override
    public Output format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int exponent = BigDecimalMath.exponent(number);
        int index = Math.floorDiv(exponent, 3);
        String[] suffixes = configuration.get(SUFFIXES);
        if(number.abs().compareTo(BigDecimal.ONE) < 0 || index < 0 || index >= suffixes.length) {
            return factory.getDefaultNumberSuffixFactory().format(number, factory, configuration);
        }
        int shift = Math.floorMod(exponent, 3);
        exponent = index * 3;
        BigDecimal mantissa = BigDecimalMath.mantissa(number);
        if(shift != 0) {
            mantissa = mantissa.movePointRight(shift);
        }
        return new Output(mantissa, suffixes[index], exponent);
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    public enum Mode {
        SI,
        SHORT,
        LONG
    }
}
