package net.aetherteam.aether.items;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemValkyrieLance extends ItemSword
{
    private int weaponDamage;

    public ItemValkyrieLance(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
        this.weaponDamage = (4 + enumtoolmaterial.getDamageVsEntity() * 2);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return 0;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        return false;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        itemstack.damageItem(2, entityliving);
        return true;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (!flag)
        {
            return;
        }

        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)entity;

        if (player.swingProgressInt == -1)
        {
            Vec3 look = player.getLookVec();
            double dist = 8.0D;
            AxisAlignedBB aabb = player.boundingBox.expand(dist, dist, dist);
            List list = world.getEntitiesWithinAABB(Entity.class, aabb);
            Entity found = null;
            double foundLen = 0.0D;

            for (Iterator i$ = list.iterator(); i$.hasNext();)
            {
                Object o = i$.next();

                if (o != player)
                {
                    Entity ent = (Entity)o;

                    if (ent.canBeCollidedWith())
                    {
                        Vec3 vec = Vec3.createVectorHelper(ent.posX - player.posX, ent.boundingBox.minY + ent.height / 2.0F - player.posY - player.getEyeHeight(), ent.posZ - player.posZ);
                        double len = vec.lengthVector();

                        if (len <= dist)
                        {
                            vec = vec.normalize();
                            double dot = look.dotProduct(vec);

                            if ((dot >= 1.0D - 0.125D / len) && (player.canEntityBeSeen(ent)))
                            {
                                if ((foundLen == 0.0D) || (len < foundLen))
                                {
                                    found = ent;
                                    foundLen = len;
                                }
                            }
                        }
                    }
                }
            }

            if (found == null)
            {
                return;
            }

            found.attackEntityFrom(DamageSource.causePlayerDamage(player), this.weaponDamage);

            if ((found instanceof EntityLiving))
            {
                itemstack.damageItem(1, (EntityLiving)found);
            }
        }
    }
}

