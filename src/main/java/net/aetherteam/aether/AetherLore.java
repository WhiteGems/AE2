package net.aetherteam.aether;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AetherLore
{
    public ItemStack stack;
    public String name;
    public String line1;
    public String line2;
    public String line3;
    public String line4;
    public String line5;
    public String line6;
    public int type;

    public AetherLore(ItemStack var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9)
    {
        this.stack = var1;
        this.name = var2;
        this.line1 = var3;
        this.line2 = var4;
        this.line3 = var5;
        this.line4 = var6;
        this.line5 = var7;
        this.line6 = var8;
        this.type = var9;
    }

    public AetherLore(int var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9)
    {
        this(new ItemStack(var1, 1, -1), var2, var3, var4, var5, var6, var7, var8, var9);
    }

    public AetherLore(Block var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9)
    {
        this(new ItemStack(var1, 1, -1), var2, var3, var4, var5, var6, var7, var8, var9);
    }

    public AetherLore(Item var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9)
    {
        this(new ItemStack(var1, 1, -1), var2, var3, var4, var5, var6, var7, var8, var9);
    }

    public boolean equals(Object var1)
    {
        return var1 == null ? this.stack == null : (var1 instanceof AetherLore ? ((AetherLore)var1).stack.itemID == this.stack.itemID && (((AetherLore)var1).stack.getItemDamage() == this.stack.getItemDamage() || this.stack.getItemDamage() == -1) : (!(var1 instanceof ItemStack) ? false : ((ItemStack)var1).itemID == this.stack.itemID && (((ItemStack)var1).getItemDamage() == this.stack.getItemDamage() || this.stack.getItemDamage() == -1)));
    }
}
