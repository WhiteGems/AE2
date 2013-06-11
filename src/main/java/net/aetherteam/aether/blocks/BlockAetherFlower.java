package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockAetherFlower extends BlockFlower implements IAetherBlock
{
    public BlockAetherFlower(int var1)
    {
        this(var1, Material.plants);
    }

    public BlockAetherFlower(int var1, Material var2)
    {
        super(var1, var2);
        this.setTickRandomly(true);
        float var3 = 0.2F;
        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int var1)
    {
        return var1 == AetherBlocks.AetherGrass.blockID || var1 == AetherBlocks.AetherDirt.blockID;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World var1, int var2, int var3, int var4)
    {
        Block var5 = blocksList[var1.getBlockId(var2, var3 - 1, var4)];
        return (var1.getFullBlockLightValue(var2, var3, var4) >= 8 || var1.canBlockSeeTheSky(var2, var3, var4)) && var5 != null && (var5.blockID == AetherBlocks.AetherDirt.blockID || var5.blockID == AetherBlocks.AetherGrass.blockID);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
