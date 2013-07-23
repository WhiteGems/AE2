package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemVampireBlade extends ItemSword
{
    private int weaponDamage;
    private static Random random = new Random();

    public ItemVampireBlade(int i)
    {
        super(i, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        setMaxDamage(EnumToolMaterial.EMERALD.getMaxUses());
        this.weaponDamage = (4 + EnumToolMaterial.EMERALD.getDamageVsEntity() * 2);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return this.weaponDamage;
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        EntityPlayer player = (EntityPlayer)entityliving1;

        if (Aether.getServerPlayer(player) == null)
        {
            return true;
        }

        if ((player.getHealth() < Aether.getServerPlayer(player).maxHealth) &&
                (entityliving.hurtTime > 0) && (entityliving.deathTime <= 0))
        {
            player.heal(1);
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public boolean isFull3D()
    {
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        itemstack.damageItem(2, entityliving);
        return true;
    }
}

