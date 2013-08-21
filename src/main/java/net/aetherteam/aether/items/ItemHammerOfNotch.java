package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.EntityNotchWave;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemHammerOfNotch extends ItemSword
{
    private float weaponDamage;
    public EntityNotchWave notchwave;

    public ItemHammerOfNotch(int i)
    {
        super(i, EnumToolMaterial.IRON);
        this.weaponDamage = 4.0F + EnumToolMaterial.IRON.getDamageVsEntity() * 2.0F;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (Aether.getServerPlayer(entityplayer) == null)
        {
            return itemstack;
        }
        else
        {
            if (Aether.getServerPlayer(entityplayer).getPlayer().capabilities.isCreativeMode)
            {
                world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
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
                world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                this.notchwave = new EntityNotchWave(world, entityplayer);
                world.spawnEntityInWorld(this.notchwave);
            }

            return itemstack;
        }
    }
}
