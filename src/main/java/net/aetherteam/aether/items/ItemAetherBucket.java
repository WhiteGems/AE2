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

    public ItemAetherBucket(int var1, int var2)
    {
        super(var1, var2);
        this.contents = var2;
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this));
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (this.contents == 0)
        {
            return super.onItemRightClick(var1, var2, var3);
        } else
        {
            float var4 = 1.0F;
            double var5 = var3.prevPosX + (var3.posX - var3.prevPosX) * (double) var4;
            double var7 = var3.prevPosY + (var3.posY - var3.prevPosY) * (double) var4 + 1.62D - (double) var3.yOffset;
            double var9 = var3.prevPosZ + (var3.posZ - var3.prevPosZ) * (double) var4;
            MovingObjectPosition var11 = this.getMovingObjectPositionFromPlayer(var2, var3, false);

            if (var11 == null)
            {
                return var1;
            } else
            {
                int var12 = var11.blockX;
                int var13 = var11.blockY;
                int var14 = var11.blockZ;

                if (var11.sideHit == 0)
                {
                    --var13;
                }

                if (var11.sideHit == 1)
                {
                    ++var13;
                }

                if (var11.sideHit == 2)
                {
                    --var14;
                }

                if (var11.sideHit == 3)
                {
                    ++var14;
                }

                if (var11.sideHit == 4)
                {
                    --var12;
                }

                if (var11.sideHit == 5)
                {
                    ++var12;
                }

                if (!var3.canCurrentToolHarvestBlock(var12, var13, var14))
                {
                    return var1;
                } else if (!var2.isAirBlock(var12, var13, var14) && var2.getBlockMaterial(var12, var13, var14).isSolid())
                {
                    return var1;
                } else
                {
                    if (var2.provider.isHellWorld && this.contents == Block.waterMoving.blockID)
                    {
                        var2.playSoundEffect(var5 + 0.5D, var7 + 0.5D, var9 + 0.5D, "random.fizz", 0.5F, 2.6F + (var2.rand.nextFloat() - var2.rand.nextFloat()) * 0.8F);

                        for (int var15 = 0; var15 < 8; ++var15)
                        {
                            var2.spawnParticle("largesmoke", (double) var12 + Math.random(), (double) var13 + Math.random(), (double) var14 + Math.random(), 0.0D, 0.0D, 0.0D);
                        }
                    } else if (!var2.isRemote && var2.provider.terrainType.getWorldTypeID() == 2 && this.contents == Block.lavaMoving.blockID)
                    {
                        var2.setBlock(var12, var13, var14, AetherBlocks.Aerogel.blockID, 0, 4);
                    } else if (!var2.isRemote && var2.getBlockId(var11.blockX, var11.blockY, var11.blockZ) == Block.glowStone.blockID && AetherBlocks.AetherPortal.tryToCreatePortal(var2, var12, var13, var14))
                    {
                        var2.setBlock(var12, var13, var14, AetherBlocks.AetherPortal.blockID, 0, 4);
                    } else
                    {
                        var2.setBlock(var12, var13, var14, this.contents, 0, 4);
                    }

                    return var3.capabilities.isCreativeMode ? var1 : new ItemStack(Item.bucketEmpty);
                }
            }
        }
    }
}
