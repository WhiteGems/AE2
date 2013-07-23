package net.aetherteam.aether.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockAetherSapling extends BlockAetherFlower implements IAetherBlock
{
    public static int sprSkyroot = 42;
    public static int sprGoldenOak = 13;
    private Object treeGenObject = null;

    protected BlockAetherSapling(int var1, Object var2)
    {
        super(var1);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
        this.treeGenObject = var2;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var1.isRemote)
        {
            return false;
        }
        else if (var5 == null)
        {
            return false;
        }
        else
        {
            ItemStack var10 = var5.getCurrentEquippedItem();

            if (var10 == null)
            {
                return false;
            }
            else if (var10.itemID != Item.dyePowder.itemID)
            {
                return false;
            }
            else if (var10.getItemDamage() != 15)
            {
                return false;
            }
            else
            {
                this.growTree(var1, var2, var3, var4, var1.rand);
                --var10.stackSize;
                return true;
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return super.canPlaceBlockAt(var1, var2, var3, var4) && this.canThisPlantGrowOnThisBlockID(var1.getBlockId(var2, var3 - 1, var4));
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int var1)
    {
        return var1 == AetherBlocks.AetherGrass.blockID || var1 == AetherBlocks.AetherDirt.blockID;
    }

    public void growTree(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!var1.isRemote)
        {
            var1.setBlock(var2, var3, var4, 0);

            if (((WorldGenerator)this.treeGenObject).generate(var1, var5, var2, var3, var4))
            {
                var1.setBlock(var2, var3, var4, this.blockID);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!var1.isRemote)
        {
            super.updateTick(var1, var2, var3, var4, var5);

            if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9 && var5.nextInt(30) == 0)
            {
                this.growTree(var1, var2, var3, var4, var5);
            }
        }
    }
}
