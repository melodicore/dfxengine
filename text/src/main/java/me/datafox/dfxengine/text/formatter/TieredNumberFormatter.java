package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.text.api.Formatter;
import me.datafox.dfxengine.text.utils.internal.NumberUtils;
import me.datafox.dfxengine.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author datafox
 */
public class TieredNumberFormatter implements Formatter<Number> {
    private final Formatter<Number> formatter;
    private final String separator;
    private final String lastSeparator;
    private final Tier[] tiers;

    public TieredNumberFormatter(Formatter<Number> formatter, String separator, String lastSeparator, Tier ... tiers) {
        BigDecimal[] temp = new BigDecimal[tiers.length];

        boolean zeroFound = false;
        for(int i = 0; i < tiers.length; i++) {
            Tier tier = tiers[i];

            int compare = tier.getLimit().compareTo(BigDecimal.ZERO);
            if(compare < 0) {
                throw new IllegalArgumentException("negative limit");
            } else if(compare == 0) {
                zeroFound = true;
            }

            for(BigDecimal bd : temp) {
                if(bd == null) {
                    break;
                }
                if(tier.getLimit().compareTo(bd) == 0) {
                    throw new IllegalArgumentException("limits not distinct");
                }
            }

            temp[i] = tier.getLimit();
        }

        if(!zeroFound) {
            throw new IllegalArgumentException("no tier with zero limit");
        }

        this.formatter = formatter;
        this.separator = separator;
        this.lastSeparator = lastSeparator;
        this.tiers = tiers;

        Arrays.sort(this.tiers);
    }

    public TieredNumberFormatter(Formatter<Number> formatter, String separator, Tier ... tiers) {
        this(formatter, separator, null, tiers);
    }

    public TieredNumberFormatter(Formatter<Number> formatter, Tier ... tiers) {
        this(formatter, ", ", " and ", tiers);
    }

    public TieredNumberFormatter(String separator, String lastSeparator, Tier ... tiers) {
        this(Number::toString, separator, lastSeparator, tiers);
    }

    public TieredNumberFormatter(String separator, Tier ... tiers) {
        this(Number::toString, separator, null, tiers);
    }

    public TieredNumberFormatter(Tier ... tiers) {
        this(Number::toString, ", ", " and ", tiers);
    }

    @Override
    public String format(Number number) {
        BigDecimal bd = NumberUtils.toBigDecimal(number);
        List<String> list = new ArrayList<>(tiers.length);

        for(Tier tier : tiers) {
            String add = null;
            if(tier.getLimit().compareTo(BigDecimal.ZERO) == 0) {
                add = formatter.format(bd);
            } else {
                BigDecimal[] result = bd.divideAndRemainder(tier.getLimit());

                if(result[0].compareTo(BigDecimal.ZERO) != 0) {
                    add = result[0].stripTrailingZeros().toPlainString();
                }

                bd = result[1];
            }

            if(add != null) {
                if(add.equals("0")) {
                    continue;
                } else if(add.equals("1")) {
                    list.add(add + tier.getSingularSuffix());
                } else {
                    list.add(add + tier.getSuffix());
                }
            }
        }

        return StringUtils.joining(list, separator, lastSeparator);
    }
}
