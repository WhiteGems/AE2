package net.aetherteam.aether.blocks;

import java.util.HashMap;
import java.util.List;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDungeonHolystone extends BlockAether
    implements IAetherBlock
{
    private HashMap icons = new HashMap();
    public static final String[] names = { "Dungeon Holystone", "Dungeon Mossy Holystone" };

    protected BlockDungeonHolystone(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(-1.0F);
        setStepSound(Block.soundStoneFootstep);
        setResistance(1000000.0F);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 3));
    }

    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.DungeonHolystone, 1, meta);
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

    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return false;
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)) == null)
        {
            world.setBlockToAir(x, y, z);
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        world.setBlockToAir(x, y, z);
    }

    public boolean isOpaqueCube()
    {
        return true;
    }
}

