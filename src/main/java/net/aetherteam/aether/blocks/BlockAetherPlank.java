package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockAetherPlank extends BlockAether implements IAetherBlock
{
    protected BlockAetherPlank(int var1, Material var2)
    {
        super(var1, var2);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }
}
