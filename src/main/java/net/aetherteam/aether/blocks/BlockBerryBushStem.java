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
    protected BlockBerryBushStem(int blockID)
    {
        super(blockID);
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        this.checkFlowerChange(world, i, j, k);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (!world.isRemote)
        {
            super.updateTick(world, i, j, k, random);

            if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0)
            {
                world.setBlock(i, j, k, AetherBlocks.BerryBush.blockID);
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox((double)i + this.minX, (double)j + this.minY, (double)k + this.minZ, (double)i + this.maxX, (double)j + this.maxY, (double)k + this.maxZ);
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityPlayer)
    {
        if (world.isRemote)
        {
            return false;
        }
        else if (entityPlayer == null)
        {
            return false;
        }
        else
        {
            ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

            if (itemStack == null)
            {
                return false;
            }
            else if (itemStack.itemID != Item.dyePowder.itemID)
            {
                return false;
            }
            else if (itemStack.getItemDamage() != 15)
            {
                return false;
            }
            else
            {
                --itemStack.stackSize;
                world.setBlock(i, j, k, 0);
                world.setBlock(i, j, k, AetherBlocks.BerryBush.blockID);
                return true;
            }
        }
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return i == AetherBlocks.AetherGrass.blockID || i == AetherBlocks.AetherDirt.blockID;
    }
}
