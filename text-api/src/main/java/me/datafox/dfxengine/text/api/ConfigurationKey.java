package me.datafox.dfxengine.text.api;

import lombok.*;

/**
 * @author datafox
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public final class ConfigurationKey<T> {
    private static int counter = 0;

    @ToString.Exclude
    private final int index = counter++;

    @EqualsAndHashCode.Exclude
    @Getter
    private T defaultValue;

    public void setDefaultValue(T defaultValue) {
        if(this.defaultValue != null) {
            throw new UnsupportedOperationException("Default value has already been set");
        }
        this.defaultValue = defaultValue;
    }

    public static <T> ConfigurationKey<T> of(T defaultValue) {
        return new ConfigurationKey<>(defaultValue);
    }

    public static <T> ConfigurationKey<T> of() {
        return new ConfigurationKey<>();
    }
}
