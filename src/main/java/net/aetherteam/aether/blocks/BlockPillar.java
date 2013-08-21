package net.aetherteam.aether.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockPillar extends BlockAether implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprTopSide;

    protected BlockPillar(int i)
    {
        super(i, Material.rock);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int j)
    {
        return i != 0 && i != 1 ? (j == 0 ? sprSide : sprTopSide) : sprTop;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int i)
    {
        return i;
    }

    public Block setIconName(String name)
    {
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        itemList.add(new ItemStack(this, 1, 0));
        itemList.add(new ItemStack(this, 1, 1));
        itemList.add(new ItemStack(this, 1, 2));
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("aether:Pillar Top");
        sprSide = par1IconRegister.registerIcon("aether:Pillar Side");
        sprTopSide = par1IconRegister.registerIcon("aether:Pillar Top Side");
    }
}
