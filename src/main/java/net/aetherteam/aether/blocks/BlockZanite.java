package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockZanite extends BlockAether
    implements IAetherBlock
{
    protected BlockZanite(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(3.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
    }

    public int getRenderColor(int i)
    {
        return 10066431;
    }
}

