package net.aetherteam.aether.items;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemContinuum extends ItemAether
{
    private static ArrayList banList = new ArrayList();

    private static Random random = new Random();

    protected ItemContinuum(int i)
    {
        super(i);
        setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        int count;

        do
        {
            count = 256 + random.nextInt(31744);
        }
        while ((net.minecraft.item.Item.itemsList[count] == null) || ((net.minecraft.item.Item.itemsList[count] != null) && ((new ItemStack(count, 1, 0).getItem() instanceof ItemBlock))) || (banList.contains(Integer.valueOf(count))));

        itemstack.itemID = count;
        return itemstack;
    }

    public static void addBan(int ID)
    {
        banList.add(Integer.valueOf(ID));
    }

    public static void removeBan(int ID)
    {
        banList.remove(ID);
    }
}

