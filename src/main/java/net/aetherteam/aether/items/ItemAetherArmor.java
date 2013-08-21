package net.aetherteam.aether.items;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemAetherArmor extends ItemArmor
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

    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
    {
        return itemstack.getItemName().contains(" Leggings") ? "aether:textures/armor/" + this.armorName + "_2.png" : "aether:textures/armor/" + this.armorName + "_1.png";
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }
}
