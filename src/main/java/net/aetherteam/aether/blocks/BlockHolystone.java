package net.aetherteam.aether.blocks;

import java.util.HashMap;
import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockHolystone extends BlockAether implements IAetherBlock
{
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Holystone", "Mossy Holystone"};

    protected BlockHolystone(int blockID)
    {
        super(blockID, Material.rock);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 3));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.Holystone, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < names.length; ++i)
        {
            this.icons.put(names[i], par1IconRegister.registerIcon("aether:" + names[i]));
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            ItemStack stack;

            if (meta == 0)
            {
                if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
                {
                    entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    stack = new ItemStack(AetherBlocks.Holystone.blockID, 2, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else if (meta == 2)
            {
                if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
                {
                    entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    stack = new ItemStack(AetherBlocks.Holystone.blockID, 2, 3);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, 3);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else
            {
                stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, meta);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }
}
