package net.aetherteam.aether.blocks;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAetherLog extends BlockAether
    implements IAetherBlock
{
    private static Random rand = new Random();
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprGoldenSide;

    protected BlockAetherLog(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(2.0F);
        setStepSound(Block.soundWoodFootstep);
    }

    public int damageDropped(int par1)
    {
        return par1;
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    public Icon getIcon(int i, int j)
    {
        if ((i <= 1) &&
                (j <= 3))
        {
            return sprTop;
        }

        if (j <= 1)
        {
            return sprSide;
        }

        return sprGoldenSide;
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            if (meta == 0)
            {
                if ((entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootAxe.itemID))
                {
                    entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                    ItemStack stack = new ItemStack(AetherBlocks.AetherLog.blockID, 2, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    ItemStack stack = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else if (meta > 1)
            {
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();

                if ((itemstack == null) || ((itemstack.itemID != AetherItems.ZaniteAxe.itemID) && (itemstack.itemID != AetherItems.GravititeAxe.itemID) && (itemstack.itemID != Item.axeDiamond.itemID) && (itemstack.itemID != Item.axeIron.itemID) && (meta >= 2)))
                {
                    return;
                }

                ItemStack stack = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                dropBlockAsItem_do(world, x, y, z, stack);
                ItemStack amberStack = new ItemStack(AetherItems.GoldenAmber.itemID, 1 + rand.nextInt(3), 0);
                dropBlockAsItem_do(world, x, y, z, amberStack);
            }
            else
            {
                ItemStack stack = new ItemStack(AetherBlocks.AetherLog.blockID, 1, meta);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("Aether:Skyroot Log Top");
        sprSide = par1IconRegister.registerIcon("Aether:Skyroot Log Side");
        sprGoldenSide = par1IconRegister.registerIcon("Aether:Golden Oak Log Side");
        super.registerIcons(par1IconRegister);
    }
}

