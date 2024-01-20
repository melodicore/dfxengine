package me.datafox.dfxengine.text.utils;

import me.datafox.dfxengine.text.formatter.Tier;

/**
 * @author datafox
 */
public class Tiers {
    private static final Tier[] TIME = {
            new Tier(0, " seconds", " second"),
            new Tier(60, " minutes", " minute"),
            new Tier(60 * 60, " hours", " hour"),
            new Tier(60 * 60 * 24, " days", " day"),
            new Tier(60 * 60 * 24 * 7, " weeks", " week"),
            new Tier(60 * 60 * 24 * 30, " months", " month"),
    };

    private static final Tier[] TIME_SHORT = {
            new Tier(0, "s"),
            new Tier(60, "m"),
            new Tier(60 * 60, "h"),
            new Tier(60 * 60 * 24, "d"),
            new Tier(60 * 60 * 24 * 7, "w"),
            new Tier(60 * 60 * 24 * 30, "m"),
    };

    public static Tier[] time() {
        return TIME;
    }

    public static Tier[] shortTime() {
        return TIME_SHORT;
    }
}
