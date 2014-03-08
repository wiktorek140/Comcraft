/*
 * Class is used to chceck collisions with blocks.
 */

package net.comcraft.src;

// ModLoader start
import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

// ModLoader end
public final class AxisAlignedBB extends JsObject { // ModLoader

    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;
    // ModLoader start
    private static final int ID_GET_BOUNDING_BOX = 100;
    private static final int ID_COLLIDES_WITH = 101;
    public static final int ID_CONSTRUCT = 102;
    // ModLoader end
    public static final JsObject AABB_PROTOTYPE = new JsObject(OBJECT_PROTOTYPE)
        .addNative("getBoundingBox", ID_GET_BOUNDING_BOX, 6)
        .addNative("collidesWith", ID_COLLIDES_WITH, 1);

    public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this(); // ModLoader
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AxisAlignedBB() {
        super(AABB_PROTOTYPE); // ModLoader
        // ModLoader start
        // Properties
        addVar("minX", new Float(minX));
        addVar("minY", new Float(minY));
        addVar("minZ", new Float(minZ));
        addVar("maxX", new Float(maxX));
        addVar("maxY", new Float(maxY));
        addVar("maxZ", new Float(maxZ));
        // ModLoader end
    }

    public static AxisAlignedBB getBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public boolean collidesWith(AxisAlignedBB axisAlignedBB) {
        Vec3D[] vecTab = new Vec3D[8];

        vecTab[0] = new Vec3D(minX, minY, minZ);
        vecTab[1] = new Vec3D(maxX, minY, minZ);
        vecTab[2] = new Vec3D(minX, maxY, minZ);
        vecTab[3] = new Vec3D(minX, minY, maxZ);
        vecTab[4] = new Vec3D(maxX, maxY, minZ);
        vecTab[5] = new Vec3D(minX, maxY, maxZ);
        vecTab[6] = new Vec3D(maxX, minY, maxZ);
        vecTab[7] = new Vec3D(maxX, maxY, maxZ);

        for (int n = 0; n < vecTab.length; ++n) {
            if (axisAlignedBB.isVecInside(vecTab[n])) {
                return true;
            }
        }

        return false;
    }

    public boolean isVecInside(Vec3D vec3D) {
        return minX <= vec3D.x && maxX >= vec3D.x && minY <= vec3D.y && maxY >= vec3D.y && minZ <= vec3D.z && maxZ >= vec3D.z;
    }

    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_CONSTRUCT:
            minX = (float) stack.getNumber(sp + 2);
            minY = (float) stack.getNumber(sp + 3);
            minZ = (float) stack.getNumber(sp + 4);
            maxX = (float) stack.getNumber(sp + 5);
            maxY = (float) stack.getNumber(sp + 6);
            maxZ = (float) stack.getNumber(sp + 7);
            break;
        case ID_GET_BOUNDING_BOX:
            stack.setObject(
                    sp,
                    getBoundingBox((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4),
                            (float) stack.getNumber(sp + 5), (float) stack.getNumber(sp + 6), (float) stack.getNumber(sp + 7)));
            break;
        case ID_COLLIDES_WITH:
            stack.setBoolean(sp, collidesWith((AxisAlignedBB) stack.getObject(sp + 2)));
            break;

        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
}
