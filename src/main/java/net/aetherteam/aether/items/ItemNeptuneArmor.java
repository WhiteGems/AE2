package net.aetherteam.aether.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemNeptuneArmor extends ItemArmor
{
    private int colour;
    public String armorName;

    public ItemNeptuneArmor(int i, EnumArmorMaterial j, int k, int l, int col, String armorName)
    {
        super(i, j, k, l);
        this.colour = col;
        this.armorName = armorName;
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int j)
    {
        return this.colour;
    }

    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
    {
        return itemstack.getItemName().contains(" Leggings") ? "aether:textures/armor/" + this.armorName + "_2.png" : "aether:textures/armor/" + this.armorName + "_1.png";
    }
}
