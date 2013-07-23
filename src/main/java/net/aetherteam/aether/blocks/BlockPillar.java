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

    protected BlockPillar(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return var1 != 0 && var1 != 1 ? (var2 == 0 ? sprSide : sprTopSide) : sprTop;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(this, 1, 0));
        var3.add(new ItemStack(this, 1, 1));
        var3.add(new ItemStack(this, 1, 2));
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        sprTop = var1.registerIcon("Aether:Pillar Top");
        sprSide = var1.registerIcon("Aether:Pillar Side");
        sprTopSide = var1.registerIcon("Aether:Pillar Top Side");
        super.registerIcons(var1);
    }
}
