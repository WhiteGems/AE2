package net.aetherteam.aether.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemZaniteSword extends ItemSword
{
    public ItemZaniteSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return super.getStrVsBlock(itemstack, block) * (2.0F * itemstack.getItemDamage() / itemstack.getItem().getMaxDamage() + 0.5F);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

