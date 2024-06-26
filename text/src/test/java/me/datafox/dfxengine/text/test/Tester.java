package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.suffix.ExponentSuffixFactory;
import me.datafox.dfxengine.text.utils.TextHandles;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public class Tester {
    @Test
    public void test() {
        Injector injector = new InjectorBuilder().build();
        TextFactory factory = injector.getComponent(TextFactory.class);
        TextHandles handles = injector.getComponent(TextHandles.class);
        factory.getConfiguration().set(ExponentSuffixFactory.INTERVAL, 3);
        NumberFormatter test = factory.getNumberFormatter(handles.getEvenLengthNumberFormatter());
        for(int i = 0; i < 5; i++) {
            for(int p = 0; p < 7; p++) {
                for(int e = 0; e < 3; e++) {
                    String s = getRandomDigit();
                    if(e != 0) {
                        s += getRandomDigit().repeat(e);
                    }
                    if(p != 0) {
                        s += "." + getRandomDigit().repeat(p);
                    }
                    if(i != 0) {
                        s += "e" + (i * 3);
                    }
                    System.out.println(test.format(new BigDecimal(s), factory, factory.getConfiguration()));
                }
            }
        }
    }

    private String getRandomDigit() {
        int d = (int) (Math.random() * 10);
        return Integer.toString(d);
    }
}
