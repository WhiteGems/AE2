package net.aetherteam.aether.blocks;

import java.util.HashMap;
import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockHolystone extends BlockAether
    implements IAetherBlock
{
    private HashMap icons = new HashMap();
    public static final String[] names = { "Holystone", "Mossy Holystone" };

    protected BlockHolystone(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(1.5F);
        setStepSound(Block.soundStoneFootstep);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 3));
    }

    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.Holystone, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < names.length; i++)
        {
            this.icons.put(names[i], par1IconRegister.registerIcon("Aether:" + names[i]));
        }
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            if (meta == 0)
            {
                if ((entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootPickaxe.itemID))
                {
                    entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                    ItemStack stack = new ItemStack(AetherBlocks.Holystone.blockID, 2, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    ItemStack stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else if (meta == 2)
            {
                if ((entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootPickaxe.itemID))
                {
                    entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                    ItemStack stack = new ItemStack(AetherBlocks.Holystone.blockID, 2, 3);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    ItemStack stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, 3);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else
            {
                ItemStack stack = new ItemStack(AetherBlocks.Holystone.blockID, 1, meta);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    public boolean isOpaqueCube()
    {
        return true;
    }
}

