package net.aetherteam.aether.oldcode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAetherBanBlock extends ItemBlock
{
    private int dimensionToBan;

    public ItemAetherBanBlock(int var1)
    {
        super(var1);
        this.dimensionToBan = 3;
    }

    public ItemAetherBanBlock(int var1, int var2)
    {
        this(var1);
        this.dimensionToBan = var2;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return var2.dimension != this.dimensionToBan ? super.onItemUse(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10) : false;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
