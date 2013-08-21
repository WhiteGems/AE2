package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCloudParachute extends ItemAether
{
    public ItemCloudParachute(int i, int damage)
    {
        super(i);
        this.maxStackSize = 1;
        this.setMaxDamage(damage);
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
            Aether.getServerPlayer(entityplayer).setParachuting(true, this.getParachuteType());
            --itemstack.stackSize;
            return itemstack;
        }
    }

    private int getParachuteType()
    {
        return this.itemID == AetherItems.CloudParachute.itemID ? 0 : (this.itemID == AetherItems.GoldenCloudParachute.itemID ? 1 : (this.itemID == AetherItems.PurpleCloudParachute.itemID ? 2 : (this.itemID == AetherItems.GreenCloudParachute.itemID ? 3 : (this.itemID == AetherItems.BlueCloudParachute.itemID ? 4 : 0))));
    }
}
