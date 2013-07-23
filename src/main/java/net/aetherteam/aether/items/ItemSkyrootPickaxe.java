package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;

public class ItemSkyrootPickaxe extends ItemPickaxe
{
    private static Random random = new Random();

    protected ItemSkyrootPickaxe(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
