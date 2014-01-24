package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

public class BlockPlayer extends Block {

    private int texFront;
    private int texBack;
    private int texArms;
    private int texHair;
    private int texFeet;

    public BlockPlayer() {
        super(255);
        texFront = 3;
        texBack = 2;
        texArms = 4;
        texHair = 0;
        texFeet = 1;
    }

    public int[] getUsedTexturesList() {
        return new int[] { texFront, texBack, texArms, texHair, texFeet };
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side == 0) {
            return texFront;
        } else if (side == 1) {
            return texBack;
        } else if (side == 2 || side == 3) {
            return texArms;
        } else if (side == 4) {
            return texHair;
        } else {
            return texFeet;
        }
    }

    public boolean collidesWithPlayer() {
        return false;
    }

    public int getRenderType() {
        return 10;
    }

    public boolean doesBlockDestroyGrass() {
        return false;
    }

    public boolean canBePieced() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public VertexBuffer[][][][] getBlockVertexBufferPieced(World world, int x, int y, int z) {
        return ModelPlayer.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPlayer.indexBuffer;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 2f, z + 1f);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return true;
    }
    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return true;
    }
}
