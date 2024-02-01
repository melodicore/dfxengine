package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.text.api.TextFactory;

/**
 * @author datafox
 */
public class TestConstants {
    public static Injector injector;

    public static TextFactory textFactory;

    public static void initializeConstants() {
        injector = InjectorBuilder
                .create()
                .build();

        textFactory = injector.getSingletonComponent(TextFactory.class);
    }
}
