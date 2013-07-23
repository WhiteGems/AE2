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

    protected BlockAetherLog(int var1)
    {
        super(var1, Material.wood);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 1));
        var3.add(new ItemStack(var1, 1, 2));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return var1 <= 1 && var2 <= 3 ? sprTop : (var2 <= 1 ? sprSide : sprGoldenSide);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);

        if (!var1.isRemote)
        {
            ItemStack var7;

            if (var6 == 0)
            {
                if (var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootAxe.itemID)
                {
                    var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    var7 = new ItemStack(AetherBlocks.AetherLog.blockID, 2, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
                else
                {
                    var7 = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
            }
            else if (var6 > 1)
            {
                var7 = var2.inventory.getCurrentItem();

                if (var7 == null || var7.itemID != AetherItems.ZaniteAxe.itemID && var7.itemID != AetherItems.GravititeAxe.itemID && var7.itemID != Item.axeDiamond.itemID && var7.itemID != Item.axeIron.itemID && var6 >= 2)
                {
                    return;
                }

                ItemStack var8 = new ItemStack(AetherBlocks.AetherLog.blockID, 1, 1);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var8);
                ItemStack var9 = new ItemStack(AetherItems.GoldenAmber.itemID, 1 + rand.nextInt(3), 0);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var9);
            }
            else
            {
                var7 = new ItemStack(AetherBlocks.AetherLog.blockID, 1, var6);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
            }
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        sprTop = var1.registerIcon("Aether:Skyroot Log Top");
        sprSide = var1.registerIcon("Aether:Skyroot Log Side");
        sprGoldenSide = var1.registerIcon("Aether:Golden Oak Log Side");
        super.registerIcons(var1);
    }
}
