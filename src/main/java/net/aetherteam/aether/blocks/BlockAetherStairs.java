package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;

public class BlockAetherStairs extends BlockStairs implements IAetherBlock
{
    private Block modelBlock;
    private int modelMeta;

    protected BlockAetherStairs(int var1, Block var2, int var3)
    {
        super(var1, var2, var3);
        this.modelBlock = var2;
        this.modelMeta = var3;
        this.setHardness(var2.blockHardness);
        this.setResistance(var2.blockResistance / 3.0F);
        this.setStepSound(var2.stepSound);
        this.setLightOpacity(0);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return this.modelBlock.getIcon(var1, this.modelMeta);
    }
}
