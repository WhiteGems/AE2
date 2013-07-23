package net.aetherteam.aether;

import cpw.mods.fml.common.ICraftingHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AetherCraftingHandler implements ICraftingHandler
{
    public void onCrafting(EntityPlayer var1, ItemStack var2, IInventory var3)
    {
        if (var2.itemID != AetherBlocks.Altar.blockID && this.isGravititeTool(var2.itemID))
        {
            ;
        }

        if (var2.itemID == Item.silk.itemID && var3 != null)
        {
            for (int var4 = 0; var4 < var3.getSizeInventory(); ++var4)
            {
                ItemStack var5 = var3.getStackInSlot(var4);

                if (var5 != null && var5.itemID == Item.shears.itemID && var5.getItemDamage() + 6 < var5.getMaxDamage())
                {
                    var3.setInventorySlotContents(var4, new ItemStack(Item.shears, 2, var5.getItemDamage() + 6));
                }
            }
        }
    }

    public void onSmelting(EntityPlayer var1, ItemStack var2) {}

    public boolean isGravititeTool(int var1)
    {
        return var1 == AetherItems.GravititePickaxe.itemID || var1 == AetherItems.GravititeAxe.itemID || var1 == AetherItems.GravititeShovel.itemID;
    }
}
