package me.datafox.dfxengine.text.formatter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;

import java.math.BigDecimal;

/**
 * @author datafox
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tier implements Comparable<Tier> {
    private final BigDecimal limit;
    private final String suffix;
    private final String singularSuffix;

    public Tier(Number limit, String suffix, String singularSuffix) {
        this(NumberUtils.toBigDecimal(limit), suffix, singularSuffix);
    }

    public Tier(Number limit, String suffix) {
        this(limit, suffix, suffix);
    }

    @Override
    public int compareTo(Tier other) {
        return -limit.compareTo(other.getLimit());
    }
}
