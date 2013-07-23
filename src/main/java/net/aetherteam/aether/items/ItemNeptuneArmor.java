package net.aetherteam.aether.items;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemNeptuneArmor extends ItemArmor
    implements IArmorTextureProvider
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
        return setUnlocalizedName("Aether:" + name);
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int j)
    {
        return this.colour;
    }

    public String getArmorTextureFile(ItemStack itemstack)
    {
        if (itemstack.getItemName().contains(" Leggings"))
        {
            return "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_2.png";
        }

        return "/net/aetherteam/aether/client/sprites/armor/" + this.armorName + "_1.png";
    }
}

