package net.aetherteam.aether.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import net.aetherteam.aether.util.WeightedRandom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemContinuum extends ItemAether
{
    private static ArrayList banList = new ArrayList();
    //private static Random random = new Random();
    private static WeightedRandom rand;
    static short[] whitelist_id = {};
    static short[] whitelist_data = {};
    static int[] whitelist_exp = {};

    protected ItemContinuum(int var1)
    {
        super(var1);
        this.setMaxStackSize(1);
    }

    static
    {
        try
        {
            String[] temp;
            temp = loadItems("/names/item_whitelist.txt");
            if (temp != null)
            {
                whitelist_id = new short[temp.length];
                whitelist_data = new short[temp.length];
                whitelist_exp = new int[temp.length];
                for (int i = 0; i < temp.length; i++)
                {
                    String[] ht = temp[i].split(":");
                    if (ht.length >= 1) whitelist_id[i] = Short.valueOf(ht[0]);
                    if (ht.length >= 2) whitelist_data[i] = Short.valueOf(ht[1]);
                    if (ht.length >= 3) whitelist_exp[i] = Integer.valueOf(ht[2]);
                }
            }
            long total = 0;
            for(int cur : whitelist_exp) total += cur;
            double[] probablity = new double[temp.length];
            for(int i = 0; i < whitelist_exp.length; ++ i) {
                probablity[i] = whitelist_exp[i] * 1.0D / total;
            }
            rand = new WeightedRandom(probablity);
        } catch (Exception e)
        { }
    }

    public static int getRandomIndex()
    {
        // return random.nextInt(whitelist_exp.length);
        return rand.nextRand();
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        int var4;

        do
        {
            var4 = whitelist_id[getRandomIndex()];
        } while (Item.itemsList[var4] == null || banList.contains(Integer.valueOf(var4)));

        var1.itemID = var4;
        if (whitelist_data[getRandomIndex()] != 0) var1.setItemDamage(whitelist_data[getRandomIndex()]);
        return var1;
    }

    private static String[] loadItems(String resname)
    {
        List<String> result = new ArrayList();
        InputStreamReader reader;
        try
        {
            reader = new InputStreamReader(ItemContinuum.class.getResourceAsStream(resname), "UTF8");
            BufferedReader buf = new BufferedReader(reader);
            String line;
            while ((line = buf.readLine()) != null) result.add(line);
            buf.close();
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return result.toArray(new String[0]);
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
