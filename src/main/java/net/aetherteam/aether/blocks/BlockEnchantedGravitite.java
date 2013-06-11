package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class BlockEnchantedGravitite extends BlockFloating implements IAetherBlock
{
    public BlockEnchantedGravitite(int var1, boolean var2)
    {
        super(var1, var2);
        this.setHardness(5.0F);
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
}
