package net.aetherteam.aether.items;

import java.util.ArrayList;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.enums.AetherEnumElement;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemElementalSword extends ItemSword
{
    public static ArrayList undead = new ArrayList();
    private int weaponDamage;
    private int holyDamage;
    private AetherEnumElement element;
    private int colour;

    public ItemElementalSword(int i, AetherEnumElement element)
    {
        super(i, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        setMaxDamage(502);
        this.weaponDamage = 4;
        this.holyDamage = 20;
        this.element = element;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        itemstack.damageItem(2, entityliving);
        return true;
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        if (this.element == AetherEnumElement.Fire)
        {
            entityliving.setFire(30);
        }
        else if (this.element == AetherEnumElement.Lightning)
        {
            if (!entityliving.worldObj.isRemote)
            {
                entityliving.worldObj.addWeatherEffect(new EntityAetherLightning(entityliving.worldObj, (int)entityliving.posX, (int)entityliving.posY, (int)entityliving.posZ, (EntityPlayer)entityliving1));
            }
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public int getDamageVsEntity(Entity entity)
    {
        EntityLiving living;

        if ((this.element == AetherEnumElement.Holy) && ((entity instanceof EntityLiving)))
        {
            living = (EntityLiving)entity;

            for (Class cls : undead)
            {
                if (living.getClass().isAssignableFrom(cls))
                {
                    return this.holyDamage;
                }
            }
        }

        return this.weaponDamage;
    }

    public boolean isFull3D()
    {
        return true;
    }

    static
    {
        undead.add(EntityZombie.class);
        undead.add(EntitySkeleton.class);
        undead.add(EntityPigZombie.class);
    }
}

