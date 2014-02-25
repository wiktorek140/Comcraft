package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsException;
import com.google.minijoe.sys.JsFunction;

public class ModGuiScreen extends GuiScreen {
    public static final int ID_CONSTRUCT = 100;
    private EventHandler eh = new EventHandler(new String[] { "initGui" });

    public ModGuiScreen(GuiScreen parentScreen) {
        super(parentScreen);
        System.out.println("ModGuiScreen");
    }

    protected void customDrawScreen() {
        // TODO Auto-generated method stub

    }

    protected void initGui() {
        JsArray eList = new JsArray();
        eh.runEvent("initGui", new Object[] { eList });
        elementsList = ModArray.toVector(eList);
        System.out.println(elementsList);
    }

    protected void handleGuiAction(GuiButton guiButton) {
        // TODO Auto-generated method stub

    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_CONSTRUCT:
            System.out.println("new GuiScreen");
            if (parCount < 1) {
                throw new JsException("[GuiScreen.init] no initGui() argument provided");
            }
            JsFunction init = (JsFunction) stack.getObject(sp + 2);
            if (init.getParameterCount() < 1) {
                throw new JsException("[GuiScreen.init] initGui() expects 1 argument, got none");
            }
            eh.bindEvent("initGui", init);
            initGui();
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
}
