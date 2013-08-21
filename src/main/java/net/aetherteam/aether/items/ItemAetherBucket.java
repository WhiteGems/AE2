package net.aetherteam.aether.items;

import java.util.ArrayList;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemAetherBucket extends ItemBucket
{
    private int contents;

    public ItemAetherBucket(int id, int isFull)
    {
        super(id, isFull);
        this.contents = isFull;
    }

    public void addCreativeItems(ArrayList itemList)
    {
        itemList.add(new ItemStack(this));
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (this.contents == 0)
        {
            return super.onItemRightClick(itemStack, world, player);
        }
        else
        {
            float var4 = 1.0F;
            double px = player.prevPosX + (player.posX - player.prevPosX) * (double)var4;
            double py = player.prevPosY + (player.posY - player.prevPosY) * (double)var4 + 1.62D - (double)player.yOffset;
            double pz = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var4;
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

            if (mop == null)
            {
                return itemStack;
            }
            else
            {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                if (mop.sideHit == 0)
                {
                    --y;
                }

                if (mop.sideHit == 1)
                {
                    ++y;
                }

                if (mop.sideHit == 2)
                {
                    --z;
                }

                if (mop.sideHit == 3)
                {
                    ++z;
                }

                if (mop.sideHit == 4)
                {
                    --x;
                }

                if (mop.sideHit == 5)
                {
                    ++x;
                }

                if (!world.canMineBlock(player, x, y, z))
                {
                    return itemStack;
                }
                else if (!world.isAirBlock(x, y, z) && world.getBlockMaterial(x, y, z).isSolid())
                {
                    return itemStack;
                }
                else
                {
                    if (world.provider.isHellWorld && this.contents == Block.waterMoving.blockID)
                    {
                        world.playSoundEffect(px + 0.5D, py + 0.5D, pz + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                        for (int var16 = 0; var16 < 8; ++var16)
                        {
                            world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                        }
                    }
                    else if (!world.isRemote && world.provider.terrainType.getWorldTypeID() == 2 && this.contents == Block.lavaMoving.blockID)
                    {
                        world.setBlock(x, y, z, AetherBlocks.Aerogel.blockID, 0, 4);
                    }
                    else if (!world.isRemote && world.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == Block.glowStone.blockID && AetherBlocks.AetherPortal.tryToCreatePortal(world, x, y, z))
                    {
                        world.setBlock(x, y, z, AetherBlocks.AetherPortal.blockID, 0, 4);
                    }
                    else
                    {
                        world.setBlock(x, y, z, this.contents, 0, 4);
                    }

                    return player.capabilities.isCreativeMode ? itemStack : new ItemStack(Item.bucketEmpty);
                }
            }
        }
    }
}
