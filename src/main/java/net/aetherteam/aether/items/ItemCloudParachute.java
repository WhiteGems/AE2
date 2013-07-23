package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
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
        if (Aether.getServerPlayer(var3) == null)
        {
            return var1;
        }
        else
        {
            Aether.getServerPlayer(var3).setParachuting(true, this.getParachuteType());
            --var1.stackSize;
            return var1;
        }
    }

    private int getParachuteType()
    {
        return this.itemID == AetherItems.CloudParachute.itemID ? 0 : (this.itemID == AetherItems.GoldenCloudParachute.itemID ? 1 : (this.itemID == AetherItems.PurpleCloudParachute.itemID ? 2 : (this.itemID == AetherItems.GreenCloudParachute.itemID ? 3 : (this.itemID == AetherItems.BlueCloudParachute.itemID ? 4 : 0))));
    }
}
