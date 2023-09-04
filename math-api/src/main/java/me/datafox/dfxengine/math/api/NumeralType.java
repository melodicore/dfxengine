package me.datafox.dfxengine.math.api;

/**
 * @author datafox
 */
public enum NumeralType {
    INT(true),
    LONG(true),
    BIG_INT(true),
    FLOAT(false),
    DOUBLE(false),
    BIG_DEC(false);

    private final boolean integer;

    NumeralType(boolean integer) {
        this.integer = integer;
    }

    public boolean isInteger() {
        return integer;
    }

    public boolean isDecimal() {
        return !integer;
    }
}
