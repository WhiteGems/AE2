package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockZanite extends BlockAether implements IAetherBlock
{
    protected BlockZanite(int blockID)
    {
        super(blockID, Material.rock);
        this.setHardness(3.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return this.getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int i)
    {
        return 10066431;
    }
}
