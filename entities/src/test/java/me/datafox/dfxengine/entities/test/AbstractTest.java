package me.datafox.dfxengine.entities.test;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.api.Injector;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author datafox
 */
public class AbstractTest {
    protected Injector injector;
    protected Engine engine;

    @BeforeEach
    public void beforeEach() {
        injector = new InjectorBuilder().build();
        engine = injector.getComponent(Engine.class);
    }

    protected void clearEngine() {
        try {
            Method method = engine.getClass().getDeclaredMethod("clear");
            method.trySetAccessible();
            method.invoke(engine);
        } catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void linkEngine() {
        try {
            Method method = engine.getClass().getDeclaredMethod("link");
            method.trySetAccessible();
            method.invoke(engine);
        } catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
