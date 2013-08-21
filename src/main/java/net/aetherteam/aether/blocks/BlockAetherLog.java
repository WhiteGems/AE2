package net.aetherteam.aether.blocks;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAetherLog extends BlockAether implements IAetherBlock
{
    private static Random rand = new Random();
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprGoldenSide;

    protected BlockAetherLog(int blockID)
    {
        super(blockID, Material.wood);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int j)
    {
        return i <= 1 && j <= 3 ? sprTop : (j <= 1 ? sprSide : sprGoldenSide);
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
                if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootAxe.itemID)
                {
                    entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    stack = new ItemStack(AetherBlocks.AetherLog.blockID, 2, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    stack = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else if (meta > 1)
            {
                stack = entityplayer.inventory.getCurrentItem();

                if (stack == null || stack.itemID != AetherItems.ZaniteAxe.itemID && stack.itemID != AetherItems.GravititeAxe.itemID && stack.itemID != Item.axeDiamond.itemID && stack.itemID != Item.axeIron.itemID && meta >= 2)
                {
                    return;
                }

                ItemStack stack1 = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                this.dropBlockAsItem_do(world, x, y, z, stack1);
                ItemStack amberStack = new ItemStack(AetherItems.GoldenAmber.itemID, 1 + rand.nextInt(3), 0);
                this.dropBlockAsItem_do(world, x, y, z, amberStack);
            }
            else
            {
                stack = new ItemStack(AetherBlocks.AetherLog.blockID, 1, meta);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("aether:Skyroot Log Top");
        sprSide = par1IconRegister.registerIcon("aether:Skyroot Log Side");
        sprGoldenSide = par1IconRegister.registerIcon("aether:Golden Oak Log Side");
        super.registerIcons(par1IconRegister);
    }
}
