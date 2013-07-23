package net.aetherteam.aether.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBerryBushStem extends BlockAetherFlower implements IAetherBlock
{
    protected BlockBerryBushStem(int var1)
    {
        super(var1);
        float var2 = 0.4F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var2 * 2.0F, 0.5F + var2);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5)
    {
        this.checkFlowerChange(var1, var2, var3, var4);
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
                var1.setBlock(var2, var3, var4, AetherBlocks.BerryBush.blockID);
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return AxisAlignedBB.getBoundingBox((double)var2 + this.minX, (double)var3 + this.minY, (double)var4 + this.minZ, (double)var2 + this.maxX, (double)var3 + this.maxY, (double)var4 + this.maxZ);
    }

    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
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
            ItemStack var6 = var5.getCurrentEquippedItem();

            if (var6 == null)
            {
                return false;
            }
            else if (var6.itemID != Item.dyePowder.itemID)
            {
                return false;
            }
            else if (var6.getItemDamage() != 15)
            {
                return false;
            }
            else
            {
                --var6.stackSize;
                var1.setBlock(var2, var3, var4, 0);
                var1.setBlock(var2, var3, var4, AetherBlocks.BerryBush.blockID);
                return true;
            }
        }
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int var1)
    {
        return var1 == AetherBlocks.AetherGrass.blockID || var1 == AetherBlocks.AetherDirt.blockID;
    }
}
