package net.aetherteam.aether.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockAetherSapling extends BlockAetherFlower
    implements IAetherBlock
{
    public static int sprSkyroot = 42;
    public static int sprGoldenOak = 13;

    private Object treeGenObject = null;

    protected BlockAetherSapling(int blockID, Object treeGen)
    {
        super(blockID);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setHardness(0.0F);
        setStepSound(Block.soundGrassFootstep);
        this.treeGenObject = treeGen;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
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

        growTree(world, x, y, z, world.rand);
        itemStack.stackSize -= 1;
        return true;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return (super.canPlaceBlockAt(world, i, j, k)) && (canThisPlantGrowOnThisBlockID(world.getBlockId(i, j - 1, k)));
    }

    protected boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return (i == AetherBlocks.AetherGrass.blockID) || (i == AetherBlocks.AetherDirt.blockID);
    }

    public void growTree(World world, int i, int j, int k, Random random)
    {
        if (world.isRemote)
        {
            return;
        }

        world.setBlock(i, j, k, 0);

        if (((WorldGenerator)this.treeGenObject).generate(world, random, i, j, k))
        {
            world.setBlock(i, j, k, this.blockID);
        }
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
            growTree(world, i, j, k, random);
        }
    }
}

