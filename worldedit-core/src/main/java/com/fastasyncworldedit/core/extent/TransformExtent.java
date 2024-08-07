package com.fastasyncworldedit.core.extent;

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.fastasyncworldedit.core.math.MutableVector3;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;

/**
 * @deprecated Unused internal, will be removed in v3
 */
@Deprecated(forRemoval = true, since = "2.9.2")
public class TransformExtent extends BlockTransformExtent {

    private final MutableVector3 mutable1 = new MutableVector3();
    private final MutableBlockVector3 mutable2 = new MutableBlockVector3();
    private BlockVector3 min;

    public TransformExtent(Extent parent) {
        super(parent);
    }

    @Override
    public ResettableExtent setExtent(Extent extent) {
        min = null;
        return super.setExtent(extent);
    }

    @Override
    public BlockVector3 getMinimumPoint() {
        BlockVector3 pos1 = getPos(super.getMinimumPoint());
        BlockVector3 pos2 = getPos(super.getMaximumPoint());
        return pos1.getMinimum(pos2);
    }

    @Override
    public BlockVector3 getMaximumPoint() {
        BlockVector3 pos1 = getPos(super.getMinimumPoint());
        BlockVector3 pos2 = getPos(super.getMaximumPoint());
        return pos1.getMaximum(pos2);
    }

    @Override
    public void setOrigin(BlockVector3 pos) {
        this.min = pos;
    }

    public BlockVector3 getPos(BlockVector3 pos) {
        if (min == null) {
            min = pos;
        }
        mutable1.mutX(pos.x() - min.x());
        mutable1.mutY(pos.y() - min.y());
        mutable1.mutZ(pos.z() - min.z());
        Vector3 tmp = getTransform().apply(mutable1);
        mutable2.mutX(tmp.x() + min.x());
        mutable2.mutY(tmp.y() + min.y());
        mutable2.mutZ(tmp.z() + min.z());
        return mutable2;
    }

    public BlockVector3 getPos(int x, int y, int z) {
        if (min == null) {
            min = BlockVector3.at(x, y, z);
        }
        mutable1.mutX(x - min.x());
        mutable1.mutY(y - min.y());
        mutable1.mutZ(z - min.z());
        Vector3 tmp = getTransform().apply(mutable1);
        mutable2.mutX(tmp.x() + min.x());
        mutable2.mutY(tmp.y() + min.y());
        mutable2.mutZ(tmp.z() + min.z());
        return tmp.toBlockPoint();
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        BlockVector3 p = getPos(x, y, z);
        return transform(super.getBlock(p.x(), p.y(), p.z()));
    }

    @Override
    public BaseBlock getFullBlock(BlockVector3 position) {
        return transform(super.getFullBlock(getPos(position)));
    }

    @Override
    public BiomeType getBiomeType(int x, int y, int z) {
        BlockVector3 p = getPos(x, y, z);
        return super.getBiomeType(p.x(), p.y(), p.z());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockStateHolder<T>> boolean setBlock(int x, int y, int z, T block)
            throws WorldEditException {
        return super.setBlock(getPos(x, y, z), transformInverse(block));
    }


    @Override
    @SuppressWarnings("unchecked")
    public <B extends BlockStateHolder<B>> boolean setBlock(BlockVector3 location, B block)
            throws WorldEditException {
        return super.setBlock(getPos(location), transformInverse(block));
    }

    @Override
    public boolean setBiome(int x, int y, int z, BiomeType biome) {
        BlockVector3 p = getPos(x, y, z);
        return super.setBiome(p.x(), p.y(), p.z(), biome);
    }

}
