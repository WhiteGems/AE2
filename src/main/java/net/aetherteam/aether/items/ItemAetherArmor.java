package net.aetherteam.aether.items;

import java.util.ArrayList;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemAetherArmor extends ItemArmor implements IArmorTextureProvider
{
    public String armorName;

    public ItemAetherArmor(int var1, EnumArmorMaterial var2, int var3, int var4, String var5)
    {
        super(var1, var2, var3, var4);
        this.armorName = var5;
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this));
    }

    public String getArmorTextureFile(ItemStack var1)
    {
        return var1.getItemName().contains(" Leggings") ? "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_2.png" : "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_1.png";
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
