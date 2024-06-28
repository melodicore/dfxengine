package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.text.*;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.TextConfigurationImpl;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.utils.Modifiers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class TextTest extends AbstractTest {
    @Test
    public void chainedTextTest() {
        ConfigurationKey<String> key = ConfigurationKey.of("yerba");
        TextConfiguration conf = new TextConfigurationImpl(factory);
        ChainedText text = new ChainedText(List.of(
                new StaticText("text 1"),
                (f, c) -> c.get(key),
                new StaticText("text 2")),
                conf);
        assertEquals("text 1 yerba text 2", factory.build(text));
        conf.set(key, "mate");
        assertEquals("text 1 mate text 2", factory.build(text));
        factory.getConfiguration().set(ChainedText.USE_LIST_DELIMITER, true);
        assertEquals("text 1, mate and text 2", factory.build(text));
        conf.set(ChainedText.USE_LIST_DELIMITER, false);
        assertEquals("text 1 mate text 2", factory.build(text));
    }

    @Test
    public void configurationTextTest() {
        ConfigurationKey<Integer> key = ConfigurationKey.of(2);
        ConfigurationText<Integer> text = new ConfigurationText<>(key, "k"::repeat);
        assertEquals("kk", factory.build(text));
        factory.getConfiguration().set(key, 5);
        assertEquals("kkkkk", factory.build(text));
    }

    @Test
    public void nameTextTest() {
        Handle handle = injector.getComponent(HandleManager.class).getOrCreateSpace("test").getOrCreateHandle("test");
        NameText<Handle> text = new NameText<>(() -> handle);
        assertEquals("test", factory.build(text));
        factory.getConfiguration().set(NameText.USE_PLURAL, true);
        assertEquals("tests", factory.build(text));
    }

    @Test
    public void numberTextTest() {
        NumberText text = new NumberText(() -> 25934.25);
        assertEquals("2.59343e4", factory.build(text));
        factory.getConfiguration().set(ConfigurationKeys.NUMBER_FORMATTER, handles.getSplittingNumberFormatter());
        assertEquals("7 hours, 12 minutes and 14 seconds", factory.build(text));
        factory.getConfiguration().set(ConfigurationKeys.NUMBER_FORMATTER, handles.getExponentSuffixFormatter());
        assertEquals("25934.25", factory.build(text));
    }

    @Test
    public void numeralTextTest() {
        NumeralText text = new NumeralText(() -> Numerals.of(25934.25));
        assertEquals("2.59343e4", factory.build(text));
        factory.getConfiguration().set(ConfigurationKeys.NUMBER_FORMATTER, handles.getSplittingNumberFormatter());
        assertEquals("7 hours, 12 minutes and 14 seconds", factory.build(text));
        factory.getConfiguration().set(ConfigurationKeys.NUMBER_FORMATTER, handles.getExponentSuffixFormatter());
        assertEquals("Double(25934.25)", factory.build(text));
    }

    @Test
    public void valueTextTest() {
        Value value = new ValueImpl(injector.getComponent(HandleManager.class).getOrCreateSpace("test").getOrCreateHandle("test"), Numerals.of(25934.25), false);
        ValueText text = new ValueText(() -> value);
        assertEquals("2.59343e4", factory.build(text));
        value.addModifier(Modifiers.exp(0));
        assertEquals("1.26377e11263", factory.build(text));
        value.set(Numerals.of(3));
        assertEquals("20", factory.build(text));
        factory.getConfiguration().set(ValueText.USE_MODIFIED, false);
        assertEquals("3", factory.build(text));
    }
}
