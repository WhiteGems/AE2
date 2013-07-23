package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockAerogel extends BlockAether
    implements IAetherBlock
{
    public BlockAerogel(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(1.0F);
        setResistance(2000.0F);
        setLightOpacity(3);
        setStepSound(Block.soundStoneFootstep);
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }
}

