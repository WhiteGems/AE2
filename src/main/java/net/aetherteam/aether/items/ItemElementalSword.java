package net.aetherteam.aether.items;

import java.util.ArrayList;
import java.util.Iterator;
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

public class ItemElementalSword extends ItemSword
{
    public static ArrayList undead = new ArrayList();
    private int weaponDamage;
    private int holyDamage;
    private AetherEnumElement element;
    private int colour;

    public ItemElementalSword(int var1, AetherEnumElement var2)
    {
        super(var1, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        this.setMaxDamage(502);
        this.weaponDamage = 4;
        this.holyDamage = 20;
        this.element = var2;
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
        return 1.5F;
    }

    public boolean onBlockDestroyed(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6)
    {
        var1.damageItem(2, var6);
        return true;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        if (this.element == AetherEnumElement.Fire)
        {
            var2.setFire(30);
        }
        else if (this.element == AetherEnumElement.Lightning && !var2.worldObj.isRemote)
        {
            var2.worldObj.addWeatherEffect(new EntityAetherLightning(var2.worldObj, (double)((int)var2.posX), (double)((int)var2.posY), (double)((int)var2.posZ), (EntityPlayer)var3));
        }

        var1.damageItem(1, var3);
        return true;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        if (this.element == AetherEnumElement.Holy && var1 instanceof EntityLiving)
        {
            EntityLiving var2 = (EntityLiving)var1;
            Iterator var3 = undead.iterator();

            while (var3.hasNext())
            {
                Class var4 = (Class)var3.next();

                if (var2.getClass().isAssignableFrom(var4))
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
