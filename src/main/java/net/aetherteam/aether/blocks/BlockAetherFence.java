package net.aetherteam.aether.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockAetherFence extends BlockFence implements IAetherBlock
{
    private Block modelBlock;
    private int modelMeta;

    public BlockAetherFence(int var1, Block var2, int var3)
    {
        super(var1, "Fence", Material.wood);
        this.modelBlock = var2;
        this.modelMeta = var3;
    }

    public BlockAetherFence(int var1, Block var2, int var3, Material var4)
    {
        super(var1, "Fence", var4);
        this.modelBlock = var2;
        this.modelMeta = var3;
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return this.modelBlock.getIcon(var1, this.modelMeta);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
