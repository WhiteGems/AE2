package net.aetherteam.aether.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBerryBushStem extends BlockAetherFlower
    implements IAetherBlock
{
    protected BlockBerryBushStem(int blockID)
    {
        super(blockID);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setHardness(0.2F);
        setStepSound(Block.soundGrassFootstep);
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        checkFlowerChange(world, i, j, k);
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isRemote)
        {
            return;
        }

        super.updateTick(world, i, j, k, random);

        if ((world.getBlockLightValue(i, j + 1, k) >= 9) && (random.nextInt(30) == 0))
        {
            world.setBlock(i, j, k, AetherBlocks.BerryBush.blockID);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i + this.minX, j + this.minY, k + this.minZ, i + this.maxX, j + this.maxY, k + this.maxZ);
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityPlayer)
    {
        if (world.isRemote)
        {
            return false;
        }

        if (entityPlayer == null)
        {
            return false;
        }

        ItemStack itemStack = entityPlayer.cd();

        if (itemStack == null)
        {
            return false;
        }

        if (itemStack.itemID != Item.dyePowder.itemID)
        {
            return false;
        }

        if (itemStack.getItemDamage() != 15)
        {
            return false;
        }

        itemStack.stackSize -= 1;
        world.setBlock(i, j, k, 0);
        world.setBlock(i, j, k, AetherBlocks.BerryBush.blockID);
        return true;
    }

    protected boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return (i == AetherBlocks.AetherGrass.blockID) || (i == AetherBlocks.AetherDirt.blockID);
    }
}

