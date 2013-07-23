package net.aetherteam.aether.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemZanitePickaxe extends ItemPickaxe
{
    protected ItemZanitePickaxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return super.getStrVsBlock(itemstack, block) * (2.0F * itemstack.getItemDamage() / itemstack.getItem().getMaxDamage() + 0.5F);
    }
}

