package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.configuration.ConfigurationImpl;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.text.api.NumberFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public class AbstractFormatterTest extends AbstractTest {
    protected NumberFormatter formatter;
    protected Configuration configuration;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        configuration = new ConfigurationImpl();
    }

    @AfterEach
    @Override
    public void afterEach() throws NoSuchFieldException, IllegalAccessException {
        super.afterEach();
        formatter = null;
        configuration = null;
    }

    protected String format(String number) {
        return format(new BigDecimal(number));
    }

    protected String format(BigDecimal number) {
        return formatter.format(number, factory, configuration);
    }
}
