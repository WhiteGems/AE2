package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockAether extends Block implements IAetherBlock
{
    private boolean isDungeonBlock = false;

    protected BlockAether(int var1, Material var2)
    {
        super(var1, var2);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public boolean isDungeonBlock()
    {
        return this.isDungeonBlock;
    }

    public BlockAether setDungeonBlock(boolean var1)
    {
        this.isDungeonBlock = var1;
        return this;
    }
}
