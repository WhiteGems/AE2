package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemGravititeAxe extends ItemAxe
{
    private static Random random = new Random();

    protected ItemGravititeAxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        float f1 = entityplayer.rotationPitch;
        float f2 = entityplayer.rotationYaw;
        double d = entityplayer.posX;
        double d1 = entityplayer.posY + 1.62D - entityplayer.yOffset;
        double d2 = entityplayer.posZ;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec3d1 = vec3d.addVector(f7 * d3, f8 * d3, f9 * d3);
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do(vec3d, vec3d1, false);

        if (movingobjectposition == null)
        {
            return itemstack;
        }

        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;
            int blockID = world.getBlockId(i, j, k);
            int metadata = world.getBlockMetadata(i, j, k);

            for (int n = 0; n < ItemAxe.blocksEffectiveAgainst.length; n++)
            {
                if (blockID == ItemAxe.blocksEffectiveAgainst[n].blockID)
                {
                    EntityFloatingBlock floating = new EntityFloatingBlock(world, i + 0.5F, j + 0.5F, k + 0.5F, blockID, metadata);

                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(floating);
                    }

                    itemstack.damageItem(4, entityplayer);
                }
            }
        }

        return itemstack;
    }
}

