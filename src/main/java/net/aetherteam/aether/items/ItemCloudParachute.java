package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
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
        setMaxDamage(damage);
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

        Aether.getServerPlayer(entityplayer).setParachuting(true, getParachuteType());
        itemstack.stackSize -= 1;
        return itemstack;
    }

    private int getParachuteType()
    {
        if (this.itemID == AetherItems.CloudParachute.itemID)
        {
            return 0;
        }

        if (this.itemID == AetherItems.GoldenCloudParachute.itemID)
        {
            return 1;
        }

        if (this.itemID == AetherItems.PurpleCloudParachute.itemID)
        {
            return 2;
        }

        if (this.itemID == AetherItems.GreenCloudParachute.itemID)
        {
            return 3;
        }

        if (this.itemID == AetherItems.BlueCloudParachute.itemID)
        {
            return 4;
        }

        return 0;
    }
}

