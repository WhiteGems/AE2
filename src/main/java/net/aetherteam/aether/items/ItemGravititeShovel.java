package net.aetherteam.aether.items;

import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemGravititeShovel extends ItemSpade
{
    private static Random random = new Random();
    public static final Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium, AetherBlocks.AetherDirt, AetherBlocks.AetherGrass};

    public ItemGravititeShovel(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        for (int var3 = 0; var3 < blocksEffectiveAgainst.length; ++var3)
        {
            if (blocksEffectiveAgainst[var3] == var2)
            {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        float var4 = var3.rotationPitch;
        float var5 = var3.rotationYaw;
        double var6 = var3.posX;
        double var8 = var3.posY + 1.62D - (double) var3.yOffset;
        double var10 = var3.posZ;
        Vec3 var12 = Vec3.createVectorHelper(var6, var8, var10);
        float var13 = MathHelper.cos(-var5 * 0.01745329F - (float) Math.PI);
        float var14 = MathHelper.sin(-var5 * 0.01745329F - (float) Math.PI);
        float var15 = -MathHelper.cos(-var4 * 0.01745329F);
        float var16 = MathHelper.sin(-var4 * 0.01745329F);
        float var17 = var14 * var15;
        float var19 = var13 * var15;
        double var20 = 5.0D;
        Vec3 var22 = var12.addVector((double) var17 * var20, (double) var16 * var20, (double) var19 * var20);
        MovingObjectPosition var23 = var2.rayTraceBlocks_do(var12, var22, false);

        if (var23 == null)
        {
            return var1;
        } else
        {
            if (var23.typeOfHit == EnumMovingObjectType.TILE)
            {
                int var24 = var23.blockX;
                int var25 = var23.blockY;
                int var26 = var23.blockZ;
                int var27 = var2.getBlockId(var24, var25, var26);
                int var28 = var2.getBlockMetadata(var24, var25, var26);

                for (int var29 = 0; var29 < blocksEffectiveAgainst.length; ++var29)
                {
                    if (var27 == blocksEffectiveAgainst[var29].blockID)
                    {
                        if (var27 == AetherBlocks.AetherGrass.blockID)
                        {
                            var27 = AetherBlocks.AetherDirt.blockID;
                        }

                        EntityFloatingBlock var30 = new EntityFloatingBlock(var2, (double) ((float) var24 + 0.5F), (double) ((float) var25 + 0.5F), (double) ((float) var26 + 0.5F), var27, var28);

                        if (!var2.isRemote)
                        {
                            var2.spawnEntityInWorld(var30);
                        }

                        var1.damageItem(4, var3);
                    }
                }
            }

            return var1;
        }
    }
}
