package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCrystalBottle extends ItemAccessory
{
    protected ItemCrystalBottle(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
        this.setMaxStackSize(1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        NBTTagCompound var11 = new NBTTagCompound();

        if (var1.hasTagCompound())
        {
            float var12 = var1.getTagCompound().getFloat("Experience");

            if (var2.isSneaking())
            {
                if (var2.experience > 0.0F)
                {
                    var1.getTagCompound().setFloat("Experience", var12 + 0.1F);
                    var2.experience -= 0.1F;
                }
                else if (var2.experienceLevel > 0)
                {
                    var1.getTagCompound().setFloat("Experience", var12 + 1.0F);
                    --var2.experienceLevel;
                    var2.experience = 1.0F;
                }
            }
            else if (var12 > 0.0F)
            {
                if (var2.experience < 1.0F)
                {
                    var1.getTagCompound().setFloat("Experience", var12 - 0.1F);
                    var2.experience += 0.1F;
                }
                else
                {
                    var1.getTagCompound().setFloat("Experience", var12 - 1.0F);
                    var2.experienceLevel = (int)((float)var2.experienceLevel + 1.0F);
                    var2.experience = 0.0F;
                }
            }

            return true;
        }
        else
        {
            var1.setTagCompound(var11);
            return false;
        }
    }
}
