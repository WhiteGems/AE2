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
        return other == null ? this.stack == null : (other instanceof AetherLore ? ((AetherLore)other).stack.itemID == this.stack.itemID && (((AetherLore)other).stack.getItemDamage() == this.stack.getItemDamage() || this.stack.getItemDamage() == -1) : (!(other instanceof ItemStack) ? false : ((ItemStack)other).itemID == this.stack.itemID && (((ItemStack)other).getItemDamage() == this.stack.getItemDamage() || this.stack.getItemDamage() == -1)));
    }
}
