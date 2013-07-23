package net.aetherteam.aether.blocks;

import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockAetherDirt extends BlockAether
    implements IAetherBlock
{
    public BlockAetherDirt(int blockID)
    {
        super(blockID, Material.ground);
        setHardness(0.5F);
        setStepSound(Block.soundGravelFootstep);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            if (meta == 0)
            {
                if ((entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootShovel.itemID))
                {
                    entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                    ItemStack stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
                else
                {
                    ItemStack stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                    dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else
            {
                ItemStack stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, meta);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    public boolean isOpaqueCube()
    {
        return true;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemStack = entityPlayer.cd();

        if (itemStack == null)
        {
            return false;
        }

        if (itemStack.itemID == AetherItems.SwettyBall.itemID)
        {
            int grassCount = 0;

            for (int x1 = x - 1; x1 <= x + 1; x1++)
            {
                for (int z1 = z - 1; z1 <= z + 1; z1++)
                {
                    if (world.getBlockId(x1, y, z1) == this.blockID)
                    {
                        if (world.getBlockId(x1, y + 1, z1) == this.blockID)
                        {
                            if (world.getBlockId(x1, y + 2, z1) == 0)
                            {
                                if (!world.isRemote)
                                {
                                    world.setBlock(x1, y + 1, z1, AetherBlocks.AetherGrass.blockID);
                                    grassCount++;
                                }
                            }
                        }
                        else if ((world.getBlockId(x1, y + 1, z1) == 0) &&
                                 (!world.isRemote))
                        {
                            world.setBlock(x1, y, z1, AetherBlocks.AetherGrass.blockID);
                            grassCount++;
                        }
                    }
                    else if ((world.getBlockId(x1, y, z1) == 0) &&
                             (world.getBlockId(x1, y - 1, z1) == this.blockID))
                    {
                        if (!world.isRemote)
                        {
                            world.setBlock(x1, y - 1, z1, AetherBlocks.AetherGrass.blockID);
                            grassCount++;
                        }
                    }
                }

                if (grassCount > 0)
                {
                    itemStack.stackSize -= 1;
                }
            }
        }

        return false;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack stack)
    {
        world.setBlock(x, y, z, this.blockID);
        world.setBlockMetadataWithNotify(x, y, z, 1, 16);
    }
}

