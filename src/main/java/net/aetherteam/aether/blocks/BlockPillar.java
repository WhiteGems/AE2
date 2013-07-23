package net.aetherteam.aether.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockPillar extends BlockAether
    implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprTopSide;

    protected BlockPillar(int i)
    {
        super(i, Material.rock);
        setHardness(0.5F);
        setStepSound(Block.soundStoneFootstep);
    }

    public Icon getIcon(int i, int j)
    {
        if ((i == 0) || (i == 1))
        {
            return sprTop;
        }

        if (j == 0)
        {
            return sprSide;
        }

        return sprTopSide;
    }

    public int damageDropped(int i)
    {
        return i;
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        itemList.add(new ItemStack(this, 1, 0));
        itemList.add(new ItemStack(this, 1, 1));
        itemList.add(new ItemStack(this, 1, 2));
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("Aether:Pillar Top");
        sprSide = par1IconRegister.registerIcon("Aether:Pillar Side");
        sprTopSide = par1IconRegister.registerIcon("Aether:Pillar Top Side");
        super.registerIcons(par1IconRegister);
    }
}

