package net.aetherteam.aether.oldcode;

import java.util.List;
import net.aetherteam.aether.items.ItemAether;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemCloudStaff extends ItemAether
{
    public ItemCloudStaff(int i)
    {
        super(i);
        this.maxStackSize = 1;
        setMaxDamage(60);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    private boolean cloudsExist(World world, EntityPlayer entityplayer)
    {
        List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.expand(128.0D, 128.0D, 128.0D));

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if ((entity1 instanceof EntityMiniCloud))
            {
                return true;
            }
        }

        return false;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!cloudsExist(world, entityplayer))
        {
            EntityMiniCloud c1 = new EntityMiniCloud(world, entityplayer, false);
            EntityMiniCloud c2 = new EntityMiniCloud(world, entityplayer, true);
            world.spawnEntityInWorld(c1);
            world.spawnEntityInWorld(c2);
            itemstack.damageItem(1, entityplayer);
        }

        return itemstack;
    }
}

