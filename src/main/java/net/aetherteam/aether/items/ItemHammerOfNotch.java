package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.aetherteam.aether.entities.EntityNotchWave;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemHammerOfNotch extends ItemSword
{
    private int weaponDamage;
    public EntityNotchWave notchwave;

    public ItemHammerOfNotch(int i)
    {
        super(i, EnumToolMaterial.IRON);
        this.weaponDamage = (4 + EnumToolMaterial.IRON.getDamageVsEntity() * 2);
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (Aether.getServerPlayer(entityplayer) == null)
        {
            return itemstack;
        }

        if (Aether.getServerPlayer(entityplayer).getPlayer().capabilities.isCreativeMode)
        {
            world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
            this.notchwave = new EntityNotchWave(world, entityplayer);
            world.spawnEntityInWorld(this.notchwave);
        }
        else if (Aether.getServerPlayer(entityplayer).setGeneralCooldown(200, itemstack.getDisplayName()))
        {
            if (Aether.proxy.getClientPlayer() != null)
            {
                Aether.getClientPlayer(Aether.proxy.getClientPlayer()).updateGeneralCooldown();
            }

            itemstack.damageItem(1, entityplayer);
            world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
            this.notchwave = new EntityNotchWave(world, entityplayer);
            world.spawnEntityInWorld(this.notchwave);
        }

        return itemstack;
    }
}

