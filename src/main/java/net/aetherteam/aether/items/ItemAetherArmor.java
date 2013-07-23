package net.aetherteam.aether.items;

import java.util.ArrayList;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemAetherArmor extends ItemArmor
    implements IArmorTextureProvider
{
    public String armorName;

    public ItemAetherArmor(int i, EnumArmorMaterial j, int k, int l, String armorname)
    {
        super(i, j, k, l);
        this.armorName = armorname;
    }

    public void addCreativeItems(ArrayList itemList)
    {
        itemList.add(new ItemStack(this));
    }

    public String getArmorTextureFile(ItemStack itemstack)
    {
        if (itemstack.getItemName().contains(" Leggings"))
        {
            return "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_2.png";
        }

        return "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_1.png";
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

