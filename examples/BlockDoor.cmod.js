var indexTop = 81;
var indexBottom = 97;
var door = new Block(128, [ indexTop, indexBottom ]);

door.blockVertexBufferSided = Model.Door[1];
door.blockIndexBuffer = Model.Door[0];
door.shouldSideBeRendered = function() {
    return true;
};
door.doesBlockDestroyGrass = false;
door.RenderType = 5;
door.canBePieced = false;
door.canBePiecedVertically = false;
door.isNormal = false;

function getDoorSide(world, x, y, z) {
    return world.getBlockMetadata(x, y, z) & 15;
}
function isDoorOpened(world, x, y, z) {
    return (world.getBlockMetadata(x, y, z) >> 4) == 1;
}
function getDoorMetadataToSave(side, opened) {
    return side | (opened ? 16 : 0);
}

door.getBlockTexture = function(world, x, y, z, side) {
    if (world == undefined) {
        return indexTop;
    }
    if (getDoorSide(world, x, y, z) == 4) {
        return indexBottom;
    } else {
        return indexTop;
    }
};

door.getBlockTransform = function(world, x, y, z, transform, side) {
    if (world == undefined || world == null) {
        return transform;
    }

    blockTransform = new Transform();
    blockTransform.set(transform);

    if (isDoorOpened(world, x, y, z)) {
        if (getDoorSide(world, x, y, z) == 5) {
            y -= 1;
        }

        if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID) {
            blockTransform.postRotate(90, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, 5);
        } else if (world.getBlockID(x + 1, y, z) == this.blockID) {
            blockTransform.postRotate(90, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, -5);
        } else if (world.getBlockID(x, y, z + 1) == this.blockID) {
            blockTransform.postTranslate(0, 0, -5);
        } else {
            blockTransform.postTranslate(0, 0, 5);
        }

        if (getDoorSide(world, x, y, z) == 5) {
            y += 1;
        }
    } else {
        if (getDoorSide(world, x, y, z) == 5) {
            y -= 1;
        }

        if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
        } else {
            blockTransform.postRotate(90, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, 0);
        }

        if (getDoorSide(world, x, y, z) == 5) {
            y += 1;
        }
    }

    return blockTransform;
};

door.blockActivated = function(world, x, y, z, player, itemStack) {
    side = getDoorSide(world, x, y, z);

    value = !isDoorOpened(world, x, y, z);

    world.setBlockMetadata(x, y, z, getDoorMetadataToSave(side, value));

    if (side == 4) {
        world.setBlockMetadata(x, y + 1, z, getDoorMetadataToSave(getDoorSide(world, x, y + 1, z), value));
    } else if (side == 5) {
        world.setBlockMetadata(x, y - 1, z, getDoorMetadataToSave(getDoorSide(world, x, y - 1, z), value));
    }

    return true;
};

door.onBlockRemoval = function(world, x, y, z) {
    side = getDoorSide(world, x, y, z);

    if (side == 4) {
        world.setBlockID(x, y + 1, z, 0);
    } else if (side == 5) {
        world.setBlockID(x, y - 1, z, 0);
    }
};

door.onBlockPlaced = function(world, x, y, z, side) {
    if (Block.prototype.canPlaceBlockAt(world, x, y - 1, z)) {
        world.setBlockMetadata(x, y, z, getDoorMetadataToSave(5, false));
        world.setBlockAndMetadata(x, y - 1, z, this.blockID, getDoorMetadataToSave(4, false));
    } else if (Block.prototype.canPlaceBlockAt(world, x, y + 1, z)) {
        world.setBlockMetadata(x, y, z, getDoorMetadataToSave(4, false));
        world.setBlockAndMetadata(x, y + 1, z, this.blockID, getDoorMetadataToSave(5, false));
    }
};

door.getCollisionBoundingBoxFromPool = function(world, x, y, z) {
    if (isDoorOpened(world, x, y, z)) {
        if (getDoorSide(world, x, y, z) == 5) {
            y -= 1;
        }

        if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID) {
            return new AxisAlignedBB(x + 0.8, y, z, x + 1, y + 1, z + 1);
        } else if (world.getBlockID(x + 1, y, z) == this.blockID) {
            return new AxisAlignedBB(x, y, z, x + 0.2, y + 1, z + 1);
        } else if (world.getBlockID(x, y, z + 1) == this.blockID) {
            return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 0.2);
        } else {
            return new AxisAlignedBB(x, y, z + 0.8, x + 1, y + 1, z + 1);
        }
    } else {
        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    }
};

door.canPlaceBlockAt = function(world, x, y, z) {
    if (!Block.prototype.canPlaceBlockAt(world, x, y, z)) {
        return false;
    }

    if (Block.prototype.canPlaceBlockAt(world, x, y - 1, z)) {
        return true;
    }
    if (Block.prototype.canPlaceBlockAt(world, x, y + 1, z)) {
        return true;
    }

    return false;
};
