package net.comcraft.src;

import net.comcraft.client.Comcraft;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsException;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsObjectFactory;
import com.google.minijoe.sys.JsSystem;
import com.simon816.minijoe.nativetypes.Transform;

public class ModAPI extends JsObject implements JsObjectFactory {

    // Function ID's
    private static final int ID_IMPORT = 1000;

    // Object ID's
    private static final int FACTORY_ID_VEC3D = 0;
    private static final int FACTORY_ID_AABB = 1;
    private static final int FACTORY_ID_INVITEMSTACK = 2;
    private static final int FACTORY_ID_BLOCK = 3;
    private static final int FACTORY_ID_TRANSFORM = 4;

    private static ModAPI instance = null;
    public static final EventHandlerAPI event = new EventHandlerAPI();

    private Comcraft cc;

    public static ModAPI getInstance(Comcraft cc) {
        if (instance == null) {
            instance = new ModAPI(cc);
        }
        return instance;
    }

    public static ModAPI getInstance() {
        if (instance == null) {
            throw new NullPointerException("First instance of ModAPI must provide net.comcraft.client.Comcraft instance");
        }
        return instance;
    }

    public ModAPI(Comcraft cc) {
        super(OBJECT_PROTOTYPE);
        this.cc = cc;
        scopeChain = JsSystem.createGlobal();
        addSingletonObjects();
        addFunctions();
        addInstantiableObjects();
        addEventHandlerEvents();
    }

    /** Variables that are singletons (already initialised) */
    private void addSingletonObjects() {
        addVar("console", new DebugConsole());
        addVar("EventHandler", event);
        addVar("Model", new ModelsList());
    }

    /** Function names */
    private void addFunctions() {
        addNative("importFile", ID_IMPORT, 2);
    }

    /** Constructible objects */
    private void addInstantiableObjects() {
        addVar("AxisAlignedBB", new JsFunction(this, FACTORY_ID_AABB, AxisAlignedBB.AABB_PROTOTYPE, AxisAlignedBB.ID_CONSTRUCT, 6));
        addVar("Vec3D", new JsFunction(this, FACTORY_ID_VEC3D, OBJECT_PROTOTYPE, Vec3D.ID_CONSTRUCT, 3));
        addVar("InvItemStack", new JsFunction(this, FACTORY_ID_INVITEMSTACK, OBJECT_PROTOTYPE, InvItemStack.ID_CONSTRUCT, 2));
        addVar("Block", new JsFunction(this, FACTORY_ID_BLOCK, Block.BLOCK_PROTOTYPE, Block.ID_CONSTRUCT, 2));
        addVar("Transform", new JsFunction(this, FACTORY_ID_TRANSFORM, Transform.TRANSFORM_PROTOTYPE, Transform.ID_CONSTRUCT, 0));
   }

    /** String names for events bindable in EventHandlerAPI */
    private void addEventHandlerEvents() {
        event.addEvent("World.Generate");
        event.addEvent("Render.Init");
        event.addEvent("Game.Command");
        event.addEvent("Language.List");
        event.addEvent("GuiMainMenu.initGui");
    }

    /** Handle all API global function calls */
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        if (id < 1000 && id >= 100) {
            throw new IllegalArgumentException("Invalid function call. You may have missed the 'new' keyword");
        }
        switch (id) {
        case ID_IMPORT:
            if (parCount < 2) {
                throw new JsException("Not enough parameters to importFile");
            }
            String pkg = "",
            file = "";
            try {
                stack.setBoolean(sp, cc.modLoader.executeModInNs(pkg = stack.getString(sp + 2), file = stack.getString(sp + 3)));
            } catch (Exception e) {
                e.printStackTrace();
                throw new JsException("Import " + pkg + "." + file + " failed: " + e.getMessage());
            }
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    /** Handle all API new instance calls (i.e 'new <ObjectName>()') */
    public JsObject newInstance(int type) {
        switch (type) {
        case FACTORY_ID_VEC3D:
            return new Vec3D();
        case FACTORY_ID_AABB:
            return new AxisAlignedBB(AxisAlignedBB.AABB_PROTOTYPE);
        case FACTORY_ID_INVITEMSTACK:
            return new InvItemStack();
        case FACTORY_ID_BLOCK:
            return new Block(Block.BLOCK_PROTOTYPE);
        case FACTORY_ID_TRANSFORM:
            return new Transform(Transform.TRANSFORM_PROTOTYPE);
        default:
            throw new IllegalArgumentException();
        }
    }

    public String toString() {
        return "[object ModAPI]";
    }
}