package net.aetherteam.aether.items;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemValkyrieAxe extends ItemAxe
{
    private int weaponDamage = 1;
    private static Random random = new Random();

    protected ItemValkyrieAxe(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        return 0;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack var1)
    {
        return EnumAction.none;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        return false;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5)
    {
        if (var5)
        {
            if (var3 instanceof EntityPlayer)
            {
                EntityPlayer var6 = (EntityPlayer) var3;

                if (var6.swingProgressInt == -1)
                {
                    Vec3 var7 = var6.getLookVec();
                    double var8 = 8.0D;
                    AxisAlignedBB var10 = var6.boundingBox.expand(var8, var8, var8);
                    List var11 = var2.getEntitiesWithinAABB(Entity.class, var10);
                    Entity var12 = null;
                    double var13 = 0.0D;
                    Iterator var15 = var11.iterator();

                    while (var15.hasNext())
                    {
                        Object var16 = var15.next();

                        if (var16 != var6)
                        {
                            Entity var17 = (Entity) var16;

                            if (var17.canBeCollidedWith())
                            {
                                Vec3 var18 = Vec3.createVectorHelper(var17.posX - var6.posX, var17.boundingBox.minY + (double) (var17.height / 2.0F) - var6.posY - (double) var6.getEyeHeight(), var17.posZ - var6.posZ);
                                double var19 = var18.lengthVector();

                                if (var19 <= var8)
                                {
                                    var18 = var18.normalize();
                                    double var21 = var7.dotProduct(var18);

                                    if (var21 >= 1.0D - 0.125D / var19 && var6.canEntityBeSeen(var17) && (var13 == 0.0D || var19 < var13))
                                    {
                                        var12 = var17;
                                        var13 = var19;
                                    }
                                }
                            }
                        }
                    }

                    if (var12 == null)
                    {
                        return;
                    }

                    var12.attackEntityFrom(DamageSource.causePlayerDamage(var6), this.weaponDamage);

                    if (var12 instanceof EntityLiving)
                    {
                        var1.damageItem(1, (EntityLiving) var12);
                    }
                }
            }
        }
    }
}
