package net.comcraft.src;

import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsObjectFactory;
import com.google.minijoe.sys.JsSystem;

public class ModGlobals implements JsObjectFactory {
    private static final int FACTORY_ID_GUISCREEN = 0;

    public static JsObject global;

    static ModGlobals instance = new ModGlobals();

    public static EventHandlerAPI event = new EventHandlerAPI();

    public static JsObject createGlobal() {
        global = JsSystem.createGlobal();
        global.addVar("GuiScreen", new JsFunction(instance, FACTORY_ID_GUISCREEN, JsObject.OBJECT_PROTOTYPE, ModGuiScreen.ID_CONSTRUCT, 1));
        return global;
    }

    public JsObject newInstance(int type) {
        switch (type) {
        case FACTORY_ID_GUISCREEN:
            return new ModGuiScreen(null);
        default:
            throw new IllegalArgumentException();
        }
    }

}
