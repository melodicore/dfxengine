package me.datafox.dfxengine.injector.test.injector.pass.multiple_equals;

import lombok.EqualsAndHashCode;

/**
 * @author datafox
 */
@EqualsAndHashCode
public class EqualsClass {
    private final String str;

    public EqualsClass() {
        str = "JustAString";
    }
}
