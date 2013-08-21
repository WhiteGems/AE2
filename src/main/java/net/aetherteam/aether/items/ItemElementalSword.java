package net.aetherteam.aether.items;

import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.enums.AetherEnumElement;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemElementalSword extends ItemSword
{
    public static ArrayList < Class <? extends EntityLiving >> undead = new ArrayList();
    private int weaponDamage;
    private int holyDamage;
    private AetherEnumElement element;
    private int colour;

    public ItemElementalSword(int i, AetherEnumElement element)
    {
        super(i, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        this.setMaxDamage(502);
        this.weaponDamage = 4;
        this.holyDamage = 20;
        this.element = element;
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
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        itemstack.damageItem(2, entityliving);
        return true;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        if (this.element == AetherEnumElement.Fire)
        {
            entityliving.setFire(30);
        }
        else if (this.element == AetherEnumElement.Lightning && !entityliving.worldObj.isRemote)
        {
            entityliving.worldObj.addWeatherEffect(new EntityAetherLightning(entityliving.worldObj, (double)((int)entityliving.posX), (double)((int)entityliving.posY), (double)((int)entityliving.posZ), (EntityPlayer)entityliving1));
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public int getDamageVsEntity(Entity entity)
    {
        if (this.element == AetherEnumElement.Holy && entity instanceof EntityLiving)
        {
            EntityLiving living = (EntityLiving)entity;
            Iterator i$ = undead.iterator();

            while (i$.hasNext())
            {
                Class cls = (Class)i$.next();

                if (living.getClass().isAssignableFrom(cls))
                {
                    return this.holyDamage;
                }
            }
        }

        return this.weaponDamage;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
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
