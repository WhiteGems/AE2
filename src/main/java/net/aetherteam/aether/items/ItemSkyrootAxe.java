package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;

public class ItemSkyrootAxe extends ItemAxe
{
    private static Random random = new Random();

    protected ItemSkyrootAxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

