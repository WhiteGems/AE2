package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockContinuumOre extends BlockAether
    implements IAetherBlock
{
    public BlockContinuumOre(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public int idDropped(int i, Random random, int k)
    {
        return AetherItems.ContinuumOrb.itemID;
    }
}

