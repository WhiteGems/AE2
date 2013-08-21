package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockAetherPlank extends BlockAether implements IAetherBlock
{
    protected BlockAetherPlank(int i, Material material)
    {
        super(i, material);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }
}
