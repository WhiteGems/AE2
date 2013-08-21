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
    public void onCrafting(EntityPlayer player, ItemStack stack, IInventory craftMatrix)
    {
        if (stack.itemID != AetherBlocks.Altar.blockID && this.isGravititeTool(stack.itemID))
        {
            ;
        }

        if (stack.itemID == Item.silk.itemID && craftMatrix != null)
        {
            for (int i = 0; i < craftMatrix.getSizeInventory(); ++i)
            {
                ItemStack stackin = craftMatrix.getStackInSlot(i);

                if (stackin != null && stackin.itemID == Item.shears.itemID && stackin.getItemDamage() + 6 < stackin.getMaxDamage())
                {
                    craftMatrix.setInventorySlotContents(i, new ItemStack(Item.shears, 2, stackin.getItemDamage() + 6));
                }
            }
        }
    }

    public void onSmelting(EntityPlayer player, ItemStack stack) {}

    public boolean isGravititeTool(int stackID)
    {
        return stackID == AetherItems.GravititePickaxe.itemID || stackID == AetherItems.GravititeAxe.itemID || stackID == AetherItems.GravititeShovel.itemID;
    }
}
