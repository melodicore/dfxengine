package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.utils.TextConfigurationImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public class AbstractFormatterTest extends AbstractTest {
    protected NumberFormatter formatter;
    protected TextConfiguration configuration;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        configuration = new TextConfigurationImpl(factory);
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
