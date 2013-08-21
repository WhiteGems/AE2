package net.aetherteam.aether.blocks;

import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class BlockAetherDirt extends BlockAether implements IAetherBlock
{
    public BlockAetherDirt(int blockID)
    {
        super(blockID, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
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
                if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
                {
                    entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else
            {
                stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, meta);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

        if (itemStack == null)
        {
            return false;
        }
        else
        {
            if (itemStack.itemID == AetherItems.SwettyBall.itemID)
            {
                int grassCount = 0;

                for (int x1 = x - 1; x1 <= x + 1; ++x1)
                {
                    for (int z1 = z - 1; z1 <= z + 1; ++z1)
                    {
                        if (world.getBlockId(x1, y, z1) == this.blockID)
                        {
                            if (world.getBlockId(x1, y + 1, z1) == this.blockID)
                            {
                                if (world.getBlockId(x1, y + 2, z1) == 0 && !world.isRemote)
                                {
                                    world.setBlock(x1, y + 1, z1, AetherBlocks.AetherGrass.blockID);
                                    ++grassCount;
                                }
                            }
                            else if (world.getBlockId(x1, y + 1, z1) == 0 && !world.isRemote)
                            {
                                world.setBlock(x1, y, z1, AetherBlocks.AetherGrass.blockID);
                                ++grassCount;
                            }
                        }
                        else if (world.getBlockId(x1, y, z1) == 0 && world.getBlockId(x1, y - 1, z1) == this.blockID && !world.isRemote)
                        {
                            world.setBlock(x1, y - 1, z1, AetherBlocks.AetherGrass.blockID);
                            ++grassCount;
                        }
                    }

                    if (grassCount > 0)
                    {
                        --itemStack.stackSize;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack stack)
    {
        world.setBlock(x, y, z, this.blockID);
        world.setBlockMetadataWithNotify(x, y, z, 1, 16);
    }
}
