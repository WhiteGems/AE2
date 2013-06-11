package net.aetherteam.aether.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemZaniteSword extends ItemSword
{
    public ItemZaniteSword(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        return super.getStrVsBlock(var1, var2) * (2.0F * (float) var1.getItemDamage() / (float) var1.getItem().getMaxDamage() + 0.5F);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
