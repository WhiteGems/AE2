package net.aetherteam.aether.items;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemColouredArmor extends ItemArmor implements IArmorTextureProvider
{
    private int colour;
    public String armorName;

    public ItemColouredArmor(int var1, EnumArmorMaterial var2, int var3, int var4, int var5, String var6)
    {
        super(var1, var2, var3, var4);
        this.colour = var5;
        this.armorName = var6;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public int getColorFromItemStack(ItemStack var1, int var2)
    {
        return this.colour;
    }

    public String getArmorTextureFile(ItemStack var1)
    {
        return var1.getItemName().contains(" Leggings") ? "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_2.png" : "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_1.png";
    }
}
