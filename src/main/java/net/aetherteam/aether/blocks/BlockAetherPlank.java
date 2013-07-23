package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockAetherPlank extends BlockAether
    implements IAetherBlock
{
    protected BlockAetherPlank(int i, Material material)
    {
        super(i, material);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(Block.soundWoodFootstep);
    }
}

