package net.aetherteam.aether.items;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemContinuum extends ItemAether
{
    private static ArrayList banList = new ArrayList();
    private static Random random = new Random();

    protected ItemContinuum(int var1)
    {
        super(var1);
        this.setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        int var4;

        do
        {
            var4 = 256 + random.nextInt(31744);
        }
        while (Item.itemsList[var4] == null || Item.itemsList[var4] != null && (new ItemStack(var4, 1, 0)).getItem() instanceof ItemBlock || banList.contains(Integer.valueOf(var4)));

        var1.itemID = var4;
        return var1;
    }

    public static void addBan(int var0)
    {
        banList.add(Integer.valueOf(var0));
    }

    public static void removeBan(int var0)
    {
        banList.remove(var0);
    }
}
