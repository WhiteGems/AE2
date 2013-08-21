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
    private static ArrayList<Integer> banList = new ArrayList();
    private static Random random = new Random();

    protected ItemContinuum(int i)
    {
        super(i);
        this.setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        int count;

        do
        {
            count = 256 + random.nextInt(31744);
        }
        while (Item.itemsList[count] == null || Item.itemsList[count] != null && (new ItemStack(count, 1, 0)).getItem() instanceof ItemBlock || banList.contains(Integer.valueOf(count)));

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
