package net.aetherteam.aether.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockAetherWall extends BlockWall implements IAetherBlock
{
    private Block modelBlock;
    private int modelMeta;

    public BlockAetherWall(int var1, Block var2, int var3)
    {
        super(var1, var2);
        this.modelBlock = var2;
        this.modelMeta = var3;
        this.setHardness(var2.blockHardness);
        this.setResistance(var2.blockResistance / 3.0F);
        this.setStepSound(var2.stepSound);
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

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
    }
}
