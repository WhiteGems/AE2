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

    public BlockAetherWall(int par1, Block par2Block, int metadata)
    {
        super(par1, par2Block);
        this.modelBlock = par2Block;
        this.modelMeta = metadata;
        this.setHardness(par2Block.blockHardness);
        this.setResistance(par2Block.blockResistance / 3.0F);
        this.setStepSound(par2Block.stepSound);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return this.modelBlock.getIcon(par1, this.modelMeta);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
}
