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

    protected ItemValkyrieAxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return 0;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        return false;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (flag)
        {
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)entity;

                if (player.swingProgress == -1.0F)
                {
                    Vec3 look = player.getLookVec();
                    double dist = 8.0D;
                    AxisAlignedBB aabb = player.boundingBox.expand(dist, dist, dist);
                    List list = world.getEntitiesWithinAABB(Entity.class, aabb);
                    Entity found = null;
                    double foundLen = 0.0D;
                    Iterator i$ = list.iterator();

                    while (i$.hasNext())
                    {
                        Object o = i$.next();

                        if (o != player)
                        {
                            Entity ent = (Entity)o;

                            if (ent.canBeCollidedWith())
                            {
                                Vec3 vec = Vec3.createVectorHelper(ent.posX - player.posX, ent.boundingBox.minY + (double)(ent.height / 2.0F) - player.posY - (double)player.getEyeHeight(), ent.posZ - player.posZ);
                                double len = vec.lengthVector();

                                if (len <= dist)
                                {
                                    vec = vec.normalize();
                                    double dot = look.dotProduct(vec);

                                    if (dot >= 1.0D - 0.125D / len && player.canEntityBeSeen(ent) && (foundLen == 0.0D || len < foundLen))
                                    {
                                        found = ent;
                                        foundLen = len;
                                    }
                                }
                            }
                        }
                    }

                    if (found == null)
                    {
                        return;
                    }

                    found.attackEntityFrom(DamageSource.causePlayerDamage(player), (float)this.weaponDamage);

                    if (found instanceof EntityLiving)
                    {
                        itemstack.damageItem(1, (EntityLiving)found);
                    }
                }
            }
        }
    }
}
