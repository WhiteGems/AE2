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

    public AetherLore(ItemStack item, String s, String s1, String s2, String s3, String s4, String s5, String s6, int i)
    {
        this.stack = item;
        this.name = s;
        this.line1 = s1;
        this.line2 = s2;
        this.line3 = s3;
        this.line4 = s4;
        this.line5 = s5;
        this.line6 = s6;
        this.type = i;
    }

    public AetherLore(int id, String s, String s1, String s2, String s3, String s4, String s5, String s6, int i)
    {
        this(new ItemStack(id, 1, -1), s, s1, s2, s3, s4, s5, s6, i);
    }

    public AetherLore(Block block, String s, String s1, String s2, String s3, String s4, String s5, String s6, int i)
    {
        this(new ItemStack(block, 1, -1), s, s1, s2, s3, s4, s5, s6, i);
    }

    public AetherLore(Item item, String s, String s1, String s2, String s3, String s4, String s5, String s6, int i)
    {
        this(new ItemStack(item, 1, -1), s, s1, s2, s3, s4, s5, s6, i);
    }

    public boolean equals(Object other)
    {
        if (other == null) return this.stack == null;
        if ((other instanceof AetherLore))
            return (((AetherLore) other).stack.itemID == this.stack.itemID) && ((((AetherLore) other).stack.getItemDamage() == this.stack.getItemDamage()) || (this.stack.getItemDamage() == -1));
        if ((other instanceof ItemStack))
            return (((ItemStack) other).itemID == this.stack.itemID) && ((((ItemStack) other).getItemDamage() == this.stack.getItemDamage()) || (this.stack.getItemDamage() == -1));
        return false;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherLore
 * JD-Core Version:    0.6.2
 */