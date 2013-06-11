package net.aetherteam.aether;

import cpw.mods.fml.common.ICraftingHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AetherCraftingHandler implements ICraftingHandler
{
    public void onCrafting(EntityPlayer player, ItemStack stack, IInventory craftMatrix)
    {
        if (stack.itemID != AetherBlocks.Altar.blockID)
        {
            if (!isGravititeTool(stack.itemID)) ;
        }
    }

    public void onSmelting(EntityPlayer player, ItemStack stack)
    {
    }

    public boolean isGravititeTool(int stackID)
    {
        return (stackID == AetherItems.GravititePickaxe.itemID) || (stackID == AetherItems.GravititeAxe.itemID) || (stackID == AetherItems.GravititeShovel.itemID);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherCraftingHandler
 * JD-Core Version:    0.6.2
 */