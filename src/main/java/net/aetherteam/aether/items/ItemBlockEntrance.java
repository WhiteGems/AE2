package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockEntrance extends ItemBlock
{
    public ItemBlockEntrance(int itemID)
    {
        super(itemID);
        this.setHasSubtypes(true);
    }

    public Item setIconName(String name)
    {
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();
        return i >= 1 ? "Dungeon Entrance Lock" : (i >= 0 ? "Dungeon Entrance" : this.getUnlocalizedName() + i);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int damage)
    {
        return damage > 1 ? 1 : 0;
    }
}
