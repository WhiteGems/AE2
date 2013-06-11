package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.FMLCommonHandler;

import java.util.Iterator;
import java.util.Random;

import net.aetherteam.aether.AetherLoot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityTreasureChest extends TileEntityChest
{
    private boolean locked = true;
    private int kind = 0;

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.locked = var1.getBoolean("locked");
        this.kind = var1.getInteger("kind");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setBoolean("locked", this.locked);
        var1.setInteger("kind", this.kind);
    }

    public void unlock(int var1)
    {
        this.kind = var1;
        Random var2 = new Random();
        int var3;

        if (var1 == 0)
        {
            for (var3 = 0; var3 < 5 + var2.nextInt(1); ++var3)
            {
                this.setInventorySlotContents(var2.nextInt(this.getSizeInventory()), AetherLoot.BRONZE.getRandomItem(var2));
            }
        }

        if (var1 == 1)
        {
            for (var3 = 0; var3 < 5 + var2.nextInt(1); ++var3)
            {
                this.setInventorySlotContents(var2.nextInt(this.getSizeInventory()), AetherLoot.SILVER.getRandomItem(var2));
            }
        }

        if (var1 == 2)
        {
            for (var3 = 0; var3 < 5 + var2.nextInt(1); ++var3)
            {
                this.setInventorySlotContents(var2.nextInt(this.getSizeInventory()), AetherLoot.GOLD.getRandomItem(var2));
            }
        }

        this.locked = false;

        if (!this.worldObj.isRemote)
        {
            this.sendToAllInOurWorld(this.getDescriptionPacket());
        }
    }

    public void onDataPacket(INetworkManager var1, Packet132TileEntityData var2)
    {
        this.readFromNBT(var2.customParam1);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    private void sendToAllInOurWorld(Packet var1)
    {
        ServerConfigurationManager var2 = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
        Iterator var3 = var2.playerEntityList.iterator();

        while (var3.hasNext())
        {
            Object var4 = var3.next();
            EntityPlayerMP var5 = (EntityPlayerMP) var4;

            if (var5.worldObj == this.worldObj)
            {
                var5.playerNetServerHandler.sendPacketToPlayer(var1);
            }
        }
    }

    public boolean isLocked()
    {
        return this.locked;
    }

    public int getKind()
    {
        return this.kind;
    }
}
