package net.aetherteam.aether.blocks;

import java.util.Random;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockContinuumOre extends BlockAether implements IAetherBlock
{
    public BlockContinuumOre(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return AetherItems.ContinuumOrb.itemID;
    }
}
