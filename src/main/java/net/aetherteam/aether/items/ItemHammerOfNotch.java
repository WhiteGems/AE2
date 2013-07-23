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
    private int weaponDamage;
    public EntityNotchWave notchwave;

    public ItemHammerOfNotch(int var1)
    {
        super(var1, EnumToolMaterial.IRON);
        this.weaponDamage = 4 + EnumToolMaterial.IRON.getDamageVsEntity() * 2;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        return 1.5F;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (Aether.getServerPlayer(var3) == null)
        {
            return var1;
        }
        else
        {
            if (Aether.getServerPlayer(var3).getPlayer().capabilities.isCreativeMode)
            {
                var2.playSoundAtEntity(var3, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                this.notchwave = new EntityNotchWave(var2, var3);
                var2.spawnEntityInWorld(this.notchwave);
            }
            else if (Aether.getServerPlayer(var3).setGeneralCooldown(200, var1.getDisplayName()))
            {
                if (Aether.proxy.getClientPlayer() != null)
                {
                    Aether.getClientPlayer(Aether.proxy.getClientPlayer()).updateGeneralCooldown();
                }

                var1.damageItem(1, var3);
                var2.playSoundAtEntity(var3, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                this.notchwave = new EntityNotchWave(var2, var3);
                var2.spawnEntityInWorld(this.notchwave);
            }

            return var1;
        }
    }
}
