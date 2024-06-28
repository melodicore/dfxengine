package me.datafox.dfxengine.text.suffix;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberSuffixFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.TextHandles;

import java.math.BigDecimal;

/**
 * A {@link NumberSuffixFormatter} that generates a suffix based on an array of suffixes and an interval, configured
 * with {@link #SUFFIXES} and {@link #INTERVAL}. The suffix with an index of the exponent scale of the number divided by
 * the interval will be used. If the exponent scale is negative or the calculated index is out of bounds, the number is
 * instead formatted with {@link TextFactory#getDefaultNumberSuffixFormatter()}. All provided preset suffixes
 * ({@link #SI}, {@link #SHORT} and {@link #LONG} use the default interval of {@code 3}. This class is designed to be
 * used with the {@link Injector}.
 *
 * @author datafox
 */
@Getter
@Component
public class NamedSuffixFormatter implements NumberSuffixFormatter {
    /**
     * Array of SI unit prefixes to be used as number suffixes.
     */
    public static final String[] SI = new String[] {
            "", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };

    /**
     * Array of abbreviated names of the short scale powers of 3 to be used as number suffixes.
     */
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

    /**
     * Array of abbreviated names of the long scale powers of 3 to be used as number suffixes.
     */
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
     * Array of suffixes to be used. The default value is {@link #SHORT}.
     */
    public static final ConfigurationKey<String[]> SUFFIXES = ConfigurationKey.of(SHORT);

    /**
     * Interval to be used between suffixes. The default value is {@code 3}.
     */
    public static final ConfigurationKey<Integer> INTERVAL = ConfigurationKey.of(3);

    private final Handle handle;

    /**
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public NamedSuffixFormatter(TextHandles handles) {
        handle = handles.getNamedSuffixFormatter();
    }

    /**
     * @param number {@inheritDoc}
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Output format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int interval = configuration.get(INTERVAL);
        int exponent = BigDecimalMath.exponent(number);
        int index = Math.floorDiv(exponent, interval);
        String[] suffixes = configuration.get(SUFFIXES);
        if(number.abs().compareTo(BigDecimal.ONE) < 0 || index < 0 || index >= suffixes.length) {
            return factory.getDefaultNumberSuffixFormatter().format(number, factory, configuration);
        }
        int shift = Math.floorMod(exponent, interval);
        exponent = index * interval;
        BigDecimal mantissa = BigDecimalMath.mantissa(number);
        if(shift != 0) {
            mantissa = mantissa.movePointRight(shift);
        }
        return new Output(mantissa, suffixes[index], exponent);
    }

    /**
     * @return {@inheritDoc}. Always returns {@code false}
     */
    @Override
    public boolean isInfinite() {
        return false;
    }
}
