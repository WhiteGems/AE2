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

    /** an array of the blocks this spade is effective against */
    public static final Block[] blocksEffectiveAgainst = new Block[] {Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium, AetherBlocks.AetherDirt, AetherBlocks.AetherGrass};

    public ItemGravititeShovel(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        for (int i = 0; i < blocksEffectiveAgainst.length; ++i)
        {
            if (blocksEffectiveAgainst[i] == par2Block)
            {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        float f1 = entityplayer.rotationPitch;
        float f2 = entityplayer.rotationYaw;
        double d = entityplayer.posX;
        double d1 = entityplayer.posY + 1.62D - (double)entityplayer.yOffset;
        double d2 = entityplayer.posZ;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f9 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = world.clip(vec3d, vec3d1);

        if (movingobjectposition == null)
        {
            return itemstack;
        }
        else
        {
            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;
                int blockID = world.getBlockId(i, j, k);
                int metadata = world.getBlockMetadata(i, j, k);

                for (int n = 0; n < blocksEffectiveAgainst.length; ++n)
                {
                    if (blockID == blocksEffectiveAgainst[n].blockID)
                    {
                        if (blockID == AetherBlocks.AetherGrass.blockID)
                        {
                            blockID = AetherBlocks.AetherDirt.blockID;
                        }

                        EntityFloatingBlock floating = new EntityFloatingBlock(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), blockID, metadata);

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
}
