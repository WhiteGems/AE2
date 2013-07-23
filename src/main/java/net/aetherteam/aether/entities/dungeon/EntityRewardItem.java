package net.aetherteam.aether.entities.dungeon;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRewardItem extends EntityItem
{
    public EntityRewardItem(World par1World, double par2, double par4, double par6, String playerName)
    {
        super(par1World);
        setPlayerName(playerName);
    }

    public EntityRewardItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack, String playerName)
    {
        super(par1World, par2, par4, par6, par8ItemStack);
        setPlayerName(playerName);
    }

    public EntityRewardItem(World par1World)
    {
        super(par1World);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, String.valueOf(""));
    }

    public void setPlayerName(String playerName)
    {
        this.dataWatcher.updateObject(16, playerName);
    }

    public String getPlayerName()
    {
        return this.dataWatcher.getWatchableObjectString(16);
    }

    private void searchForOtherItemsNearby()
    {
    }

    public boolean combineItems(EntityRewardItem par1EntityItem)
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setString("PlayerName", getPlayerName());
    }

    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        setPlayerName(tag.getString("PlayerName"));
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote)
        {
            if ((getPlayerName() != null) && (par1EntityPlayer.username.equalsIgnoreCase(getPlayerName())))
            {
                super.onCollideWithPlayer(par1EntityPlayer);
            }
        }
    }
}

