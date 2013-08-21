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
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return super.getStrVsBlock(itemstack, block) * (2.0F * (float)itemstack.getItemDamage() / (float)itemstack.getItem().getMaxDamage() + 0.5F);
    }
}
