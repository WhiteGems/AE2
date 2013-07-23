package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class BlockEnchantedGravitite extends BlockFloating
    implements IAetherBlock
{
    public BlockEnchantedGravitite(int i, boolean bool)
    {
        super(i, bool);
        setHardness(5.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
    }
}

