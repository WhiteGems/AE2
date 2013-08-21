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

    public BlockAetherFence(int id, Block block, int metadata)
    {
        super(id, "Fence", Material.wood);
        this.modelBlock = block;
        this.modelMeta = metadata;
    }

    public BlockAetherFence(int par1, Block block, int metadata, Material par3Material)
    {
        super(par1, "Fence", par3Material);
        this.modelBlock = block;
        this.modelMeta = metadata;
    }

    public void addCreativeItems(ArrayList itemList)
    {
        itemList.add(new ItemStack(this));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        return this.modelBlock.getIcon(side, this.modelMeta);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }
}
