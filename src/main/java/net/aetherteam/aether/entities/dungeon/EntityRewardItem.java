package net.aetherteam.aether.entities.dungeon;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRewardItem extends EntityItem
{
    public EntityRewardItem(World var1, double var2, double var4, double var6, String var8)
    {
        super(var1);
        this.setPlayerName(var8);
    }

    public EntityRewardItem(World var1, double var2, double var4, double var6, ItemStack var8, String var9)
    {
        super(var1, var2, var4, var6, var8);
        this.setPlayerName(var9);
    }

    public EntityRewardItem(World var1)
    {
        super(var1);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, String.valueOf(""));
    }

    public void setPlayerName(String var1)
    {
        this.dataWatcher.updateObject(16, var1);
    }

    public String getPlayerName()
    {
        return this.dataWatcher.getWatchableObjectString(16);
    }

    private void searchForOtherItemsNearby() {}

    public boolean combineItems(EntityRewardItem var1)
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setString("PlayerName", this.getPlayerName());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setPlayerName(var1.getString("PlayerName"));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {
        if (!this.worldObj.isRemote && this.getPlayerName() != null && var1.username.equalsIgnoreCase(this.getPlayerName()))
        {
            super.onCollideWithPlayer(var1);
        }
    }
}
