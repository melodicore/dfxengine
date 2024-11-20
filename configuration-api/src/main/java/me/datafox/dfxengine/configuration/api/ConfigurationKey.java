package me.datafox.dfxengine.configuration.api;

import lombok.*;

/**
 * Data class that is used as a key for {@link Configuration}. Contains a hidden index to keep the hashcode unique and a
 * default value that is used when the configured value is not set or cannot be obtained.
 *
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

    /**
     * Default value for this configuration key.
     */
    @EqualsAndHashCode.Exclude
    @Getter
    private T defaultValue;

    /**
     * Sets the default value for this configuration key.
     *
     * @param defaultValue value to be set as the default value
     * @throws UnsupportedOperationException if the default value has already been set
     */
    public void setDefaultValue(T defaultValue) {
        if(this.defaultValue != null) {
            throw new UnsupportedOperationException("Default value has already been set");
        }
        this.defaultValue = defaultValue;
    }

    /**
     * Returns a configuration key with the specified default value.
     *
     * @param defaultValue default value for the configuration key
     * @return new {@link ConfigurationKey} with the specified default value
     * @param <T> type of the value of this configuration key
     */
    public static <T> ConfigurationKey<T> of(T defaultValue) {
        return new ConfigurationKey<>(defaultValue);
    }

    /**
     * Returns a configuration key without a default value. Any class calling this method must also call
     * {@link #setDefaultValue(Object)} on the returned configuration key.
     *
     * @return new {@link ConfigurationKey} with the specified default value
     * @param <T> type of the value of this configuration key
     */
    public static <T> ConfigurationKey<T> of() {
        return new ConfigurationKey<>();
    }
}
