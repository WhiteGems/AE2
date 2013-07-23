package net.aetherteam.aether.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockAetherWall extends BlockWall
    implements IAetherBlock
{
    private Block modelBlock;
    private int modelMeta;

    public BlockAetherWall(int par1, Block par2Block, int metadata)
    {
        super(par1, par2Block);
        this.modelBlock = par2Block;
        this.modelMeta = metadata;
        setHardness(par2Block.blockHardness);
        setResistance(par2Block.blockResistance / 3.0F);
        setStepSound(par2Block.stepSound);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public Icon getIcon(int par1, int par2)
    {
        return this.modelBlock.getIcon(par1, this.modelMeta);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
}

