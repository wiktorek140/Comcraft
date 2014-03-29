package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public class DebugConsole extends JsObject {

    private static final int ID_LOG = 100;
    private static final int ID_DEBUG = 101;
    private static final int ID_INFO = 102;
    private static final int ID_WARN = 103;
    private static final int ID_ERROR = 104;

    public DebugConsole() {
        super(JsObject.OBJECT_PROTOTYPE);
        addNative("log", ID_LOG, 1);
        addNative("debug", ID_DEBUG, 1);
        addNative("info", ID_INFO, 1);
        addNative("warn", ID_WARN, 1);
        addNative("error", ID_ERROR, 1);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        String prefix = null;
        switch (id) {
        case ID_LOG:
            break;
        case ID_DEBUG:
            prefix = "debug";
            break;
        case ID_INFO:
            prefix = "info";
            break;
        case ID_WARN:
            prefix = "warining";
            break;
        case ID_ERROR:
            prefix = "error";
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
            return;
        }
        System.out.print((prefix != null) ? "[" + prefix + "] " : "");
        for (int i = 0; i < parCount; i++) {
            System.out.print((i > 0 ? " " : "") + stack.getObject(sp + 2 + i));
        }
        System.out.print('\n');
    }
}
