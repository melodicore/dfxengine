package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class TestHandles {
    public static HandleManager handleManager;
    public static Space space;
    public static Handle handle;
    public static Handle intHandle;
    public static Handle longHandle;
    public static Handle bigIntHandle;
    public static Handle floatHandle;
    public static Handle doubleHandle;
    public static Handle bigDecHandle;

    public static void initializeHandles() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
        space = handleManager.createSpace("test");
        handle = space.createHandle("handle");
        intHandle = space.createHandle("int");
        longHandle = space.createHandle("long");
        bigIntHandle = space.createHandle("bigInt");
        floatHandle = space.createHandle("float");
        doubleHandle = space.createHandle("double");
        bigDecHandle = space.createHandle("bigDec");
    }
}
