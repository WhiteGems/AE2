package net.aetherteam.aether.oldcode;

import java.util.List;

import net.aetherteam.aether.items.ItemAether;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCloudStaff extends ItemAether
{
    public ItemCloudStaff(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
        this.setMaxDamage(60);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    private boolean cloudsExist(World var1, EntityPlayer var2)
    {
        List var3 = var1.getEntitiesWithinAABBExcludingEntity(var2, var2.boundingBox.expand(128.0D, 128.0D, 128.0D));

        for (int var4 = 0; var4 < var3.size(); ++var4)
        {
            Entity var5 = (Entity) var3.get(var4);

            if (var5 instanceof EntityMiniCloud)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (!this.cloudsExist(var2, var3))
        {
            EntityMiniCloud var4 = new EntityMiniCloud(var2, var3, false);
            EntityMiniCloud var5 = new EntityMiniCloud(var2, var3, true);
            var2.spawnEntityInWorld(var4);
            var2.spawnEntityInWorld(var5);
            var1.damageItem(1, var3);
        }

        return var1;
    }
}
