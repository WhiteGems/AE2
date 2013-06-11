package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockZanite extends BlockAether implements IAetherBlock
{
    protected BlockZanite(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4)
    {
        return this.getRenderColor(var1.getBlockMetadata(var2, var3, var4));
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int var1)
    {
        return 10066431;
    }
}
