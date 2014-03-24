package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public final class Vec3D extends JsObject { // ModLoader

    public float x;
    public float y;
    public float z;

    // ModLoader start
    private static final int ID_SET_COMPONENTS = 100;
    private static final int ID_SUBTRACT_VECTOR = 101;
    private static final int ID_NORMALIZE = 102;
    private static final int ID_DOT_PRODUCT = 103;
    private static final int ID_CROSS_PRODUCT = 104;
    private static final int ID_ADD_VECTOR = 105;
    private static final int ID_DISTANCE_TO = 106;
    private static final int ID_SQUARE_DISTANCE_TO = 107;
    private static final int ID_LENGTH_VECTOR = 108;
    private static final int ID_COPY = 109;
    private static final int ID_INNER_PRODUCT = 110;
    public static final int ID_CONSTRUCT = 111;
    private static final int ID_X = 112;
    private static final int ID_Y = 114;
    private static final int ID_Z = 116;

    // ModLoader end
    public static final JsObject VEC3D_PROTOTYPE = new JsObject(OBJECT_PROTOTYPE).addNative("setComponents", ID_SET_COMPONENTS, 3)
            .addNative("subtractVector", ID_SUBTRACT_VECTOR, 1).addNative("normalize", ID_NORMALIZE, 0).addNative("dotProduct", ID_DOT_PRODUCT, 1)
            .addNative("crossProduct", ID_CROSS_PRODUCT, 1).addNative("addVector", ID_ADD_VECTOR, 3).addNative("distanceTo", ID_DISTANCE_TO, 1)
            .addNative("squareDistanceTo", ID_SQUARE_DISTANCE_TO, 1).addNative("lengthVector", ID_LENGTH_VECTOR, 0).addNative("copy", ID_COPY, 1)
            .addNative("innerProduct", ID_INNER_PRODUCT, 1).addNative("x", ID_X, -1).addNative("y", ID_Y, -1).addNative("z", ID_Z, -1);

    public Vec3D() {
        super(VEC3D_PROTOTYPE);
    }

    public Vec3D(float x, float y, float z) {
        this(); // ModLoader
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(Vec3D vec) {
        this(); // ModLoader
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3D setComponents(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3D subtractVector(Vec3D vec3D) {
        return new Vec3D(vec3D.x - x, vec3D.y - y, vec3D.z - z);
    }

    public Vec3D normalize() {
        float length = lengthVector();

        if (length < 0.0001f) {
            return new Vec3D(0, 0, 0);
        } else {
            return new Vec3D(x / length, y / length, z / length);
        }
    }

    public double dotProduct(Vec3D vec3D) {
        return x * vec3D.x + y * vec3D.y + z * vec3D.z;
    }

    public Vec3D crossProduct(Vec3D vec3D) {
        return new Vec3D(y * vec3D.z - z * vec3D.y, z * vec3D.x - x * vec3D.z, x * vec3D.y - y * vec3D.x);
    }

    public Vec3D crossProduct(float t) {
        return new Vec3D(x * t, y * t, z * t);
    }

    public Vec3D addVector(float x, float y, float z) {
        return new Vec3D(x + x, y + y, z + z);
    }

    public Vec3D addVector(Vec3D vec) {
        return new Vec3D(x + vec.x, y + vec.y, z + vec.z);
    }

    public float distanceTo(Vec3D vec3D) {
        float f = vec3D.x - x;
        float f1 = vec3D.y - y;
        float f2 = vec3D.z - z;
        return (float) Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public double squareDistanceTo(Vec3D vec3D) {
        float f = vec3D.x - x;
        float f1 = vec3D.y - y;
        float f2 = vec3D.z - z;
        return f * f + f1 * f1 + f2 * f2;
    }

    public float lengthVector() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void copy(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public float innerProduct(Vec3D vec) {
        return (x * vec.x + y * vec.y + z * vec.z);
    }

    public Vec3D subtractVector() {
        Vec3D res = new Vec3D();

        res.x = -x;
        res.y = -y;
        res.z = -z;

        return res;
    }

    public String toString() {
        return "Vec3D(x=" + x + ", y=" + y + ", z=" + z + ")";
    }

    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        Object object;
        switch (id) {
        case ID_X:
            stack.setNumber(sp, x);
            break;
        case ID_X + 1:
            x = (float) stack.getNumber(sp);
            break;
        case ID_Y:
            stack.setNumber(sp, y);
            break;
        case ID_Y + 1:
            y = (float) stack.getNumber(sp);
            break;
        case ID_Z:
            stack.setNumber(sp, z);
            break;
        case ID_Z + 1:
            z = (float) stack.getNumber(sp);
            break;
        case ID_SET_COMPONENTS:
            stack.setObject(sp, setComponents((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4)));
            break;
        case ID_SUBTRACT_VECTOR:
            if ((object = stack.getObject(sp + 2)) instanceof Vec3D) {
                stack.setObject(sp, subtractVector((Vec3D) object));
            } else {
                stack.setObject(sp, subtractVector());
            }
            break;
        case ID_NORMALIZE:
            stack.setObject(sp, normalize());
            break;
        case ID_DOT_PRODUCT:
            stack.setNumber(sp, dotProduct((Vec3D) stack.getObject(sp + 2)));
            break;
        case ID_CROSS_PRODUCT:
            if ((object = stack.getObject(sp + 2)) instanceof Vec3D) {
                stack.setObject(sp, crossProduct((Vec3D) object));
            } else {
                stack.setObject(sp, crossProduct((float) stack.getNumber(sp + 2)));
            }
            break;
        case ID_ADD_VECTOR:
            if ((object = stack.getObject(sp + 2)) instanceof Vec3D) {
                stack.setObject(sp, addVector((Vec3D) object));
            } else {
                stack.setObject(sp, addVector((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4)));
            }
            break;
        case ID_DISTANCE_TO:
            stack.setNumber(sp, (double) distanceTo((Vec3D) stack.getObject(sp + 2)));
            break;
        case ID_SQUARE_DISTANCE_TO:
            stack.setNumber(sp, squareDistanceTo((Vec3D) stack.getObject(sp + 2)));
            break;
        case ID_LENGTH_VECTOR:
            stack.setNumber(sp, (double) lengthVector());
            break;
        case ID_COPY:
            copy((Vec3D) stack.getObject(sp + 2));
            break;
        case ID_INNER_PRODUCT:
            stack.setNumber(sp, (double) innerProduct((Vec3D) stack.getObject(sp + 2)));
            break;
        case ID_CONSTRUCT:
            x = (float) stack.getNumber(sp + 2);
            y = (float) stack.getNumber(sp + 3);
            z = (float) stack.getNumber(sp + 4);
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
}
