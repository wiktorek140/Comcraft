
package net.comcraft.src;

// ModLoader start
import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;
// ModLoader end
public class InventoryPlayer extends JsObject { // ModLoader

    private int selectedElement;
    private InvItemStack[] elementsList;
    // ModLoader start
    private static final int ID_GET_SELECTED_ELEMENT_NUM = 100;
    private static final int ID_GET_SELECTED_ITEM_STACK = 101;
    private static final int ID_SET_SELECTED_ELEMENT = 102;
    private static final int ID_GET_ITEM_STACK_AT = 103;
    private static final int ID_SET_ITEM_STACK_AT = 104;
    private static final int ID_GET_FAST_SLOT_SIZE = 105;
    // ModLoader end
    
    public InventoryPlayer() {
        super(JsObject.OBJECT_PROTOTYPE); // ModLoader
        selectedElement = 0;
        elementsList = new InvItemStack[3];
        elementsList[0] = new InvItemStack(1, 1);
        elementsList[1] = new InvItemStack(2, 1);
        elementsList[2] = new InvItemStack(3, 1);
        // ModLoader start
        // Methods
        addNative("getSelectedElementNum", ID_GET_SELECTED_ELEMENT_NUM, 0);
        addNative("getSelectedItemStack", ID_GET_SELECTED_ITEM_STACK, 0);
        addNative("setSelectedElement", ID_SET_SELECTED_ELEMENT, 1);
        addNative("getItemStackAt", ID_GET_ITEM_STACK_AT, 1);
        addNative("setItemStackAt", ID_SET_ITEM_STACK_AT, 2);
        addNative("getFastSlotSize", ID_GET_FAST_SLOT_SIZE, 0);
        // Properties
        // ModLoader end
    }
    
    public int getSelectedElementNum() {
        return selectedElement;
    }
    
    public InvItemStack getSelectedItemStack() {
        return elementsList[selectedElement];
    }
    
    public void setSelectedElement(int element) {
        selectedElement = element;
    }
    
    public InvItemStack getItemStackAt(int index) {
        if (index < 0 || index >= elementsList.length) {
            return null;
        }
        
        return elementsList[index];
    }
    
    public void setItemStackAt(int index, InvItemStack itemStack) {
        if (index < 0 || index >= elementsList.length) {
            return;
        }
        
        elementsList[index] = itemStack;
    }
    
    public int getFastSlotSize() {
        return 3;
    }
    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch(id) {
        case ID_GET_SELECTED_ELEMENT_NUM:
            stack.setInt(sp,getSelectedElementNum());
            break;
        case ID_GET_SELECTED_ITEM_STACK:
            stack.setObject(sp,getSelectedItemStack());
            break;
        case ID_SET_SELECTED_ELEMENT:
            setSelectedElement(stack.getInt(sp+2));
            break;
        case ID_GET_ITEM_STACK_AT:
            stack.setObject(sp,getItemStackAt(stack.getInt(sp+2)));
            break;
        case ID_SET_ITEM_STACK_AT:
            setItemStackAt(stack.getInt(sp+2), (InvItemStack) stack.getObject(sp+3));
            break;
        case ID_GET_FAST_SLOT_SIZE:
            stack.setInt(sp,getFastSlotSize());
            break;

            default:
                super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
    
}
