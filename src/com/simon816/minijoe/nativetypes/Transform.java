package com.simon816.minijoe.nativetypes;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public class Transform extends JsObject {
    public static final int ID_CONSTRUCT = 100;
    private static final int ID_POST_TRANSLATE = 101;
    private static final int ID_POST_ROTATE = 102;
    private static final int ID_POST_SCALE = 103;
    private static final int ID_SET = 104;
    private javax.microedition.m3g.Transform tr;
    public static final JsObject TRANSFORM_PROTOTYPE = new Transform(OBJECT_PROTOTYPE).addNative("postTranslate", ID_POST_TRANSLATE, 3)
            .addNative("postRotate", ID_POST_ROTATE, 4).addNative("postScale", ID_POST_SCALE, 3).addNative("set", ID_SET, 1);

    public Transform(JsObject __proto__) {
        super(__proto__);
    }

    public Transform(javax.microedition.m3g.Transform transform) {
        this(TRANSFORM_PROTOTYPE);
        tr = transform;
    }

    public void postTranslate(float arg0, float arg1, float arg2) {
        tr.postTranslate(arg0, arg1, arg2);
    }

    public void postRotate(float arg0, float arg1, float arg2, float arg3) {
        tr.postRotate(arg0, arg1, arg2, arg3);
    }

    public void set(Transform transform) {
        tr.set(transform._getTransform());
    }

    public void postScale(float arg0, float arg1, float arg2) {
        tr.postScale(arg0, arg1, arg2);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_CONSTRUCT:
            tr = new javax.microedition.m3g.Transform();
            break;
        case ID_POST_ROTATE:
            postRotate((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4), (float) stack.getNumber(sp + 5));
            break;
        case ID_POST_TRANSLATE:
            postTranslate((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4));
            break;
        case ID_POST_SCALE:
            postScale((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4));
            break;
        case ID_SET:
            set((Transform) stack.getObject(sp + 2));
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    public javax.microedition.m3g.Transform _getTransform() {
        return tr;
    }
}