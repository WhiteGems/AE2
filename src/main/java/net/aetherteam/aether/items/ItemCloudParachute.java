package net.aetherteam.aether.items;

import net.aetherteam.aether.entities.EntityCloudParachute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCloudParachute extends ItemAether
{
    public ItemCloudParachute(int var1, int var2)
    {
        super(var1);
        this.maxStackSize = 1;
        this.setMaxDamage(var2);
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
        EntityCloudParachute var4 = new EntityCloudParachute(var2, var3, this.itemID == AetherItems.GoldenCloudParachute.itemID);

        if (!var2.isRemote)
        {
            var2.spawnEntityInWorld(var4);
        }

        var1.damageItem(1, var3);
        return var1;
    }
}
