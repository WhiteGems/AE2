package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBerryBush extends BlockAetherFlower
    implements IAetherBlock
{
    protected BlockBerryBush(int var1)
    {
        super(var1);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(0.2F);
        setStepSound(Block.soundGrassFootstep);
    }

    protected final void checkBushChange(World world, int x, int y, int z)
    {
        if (!canBlockStay(world, x, y, z))
        {
            world.setBlock(x, y, z, 0);
            ItemStack stack = new ItemStack(AetherBlocks.BerryBush.blockID, 1, 0);
            dropBlockAsItem_do(world, x, y, z, stack);
            world.setBlock(x, y, z, 0);
        }
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return AetherBlocks.BerryBush.blockID;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i + this.minX, j + this.minY, k + this.minZ, i + this.maxX, j + this.maxY, k + this.maxZ);
    }

    public int getRenderType()
    {
        return AetherBlocks.berryBushRenderId;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        int max;
        int min;
        int max;

        if ((world.getBlockId(x, y - 1, z) == AetherBlocks.AetherGrass.blockID) && (world.getBlockMetadata(x, y - 1, z) == 1))
        {
            int min = 1;
            max = 3;
        }
        else
        {
            min = 0;
            max = 2;
        }

        int randomNum = world.rand.nextInt(max - min + 1) + min;
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (randomNum != 0)
        {
            ItemStack stack = new ItemStack(AetherItems.BlueBerry.itemID, randomNum, 0);
            dropBlockAsItem_do(world, x, y, z, stack);
        }

        world.setBlock(x, y, z, AetherBlocks.BerryBushStem.blockID);
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        checkBushChange(par1World, par2, par3, par4);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        checkBushChange(par1World, par2, par3, par4);
    }
}

