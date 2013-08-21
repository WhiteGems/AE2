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
    public void readFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readFromNBT(par1nbtTagCompound);
        this.locked = par1nbtTagCompound.getBoolean("locked");
        this.kind = par1nbtTagCompound.getInteger("kind");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setBoolean("locked", this.locked);
        par1nbtTagCompound.setInteger("kind", this.kind);
    }

    public void unlock(int kind)
    {
        this.kind = kind;
        Random random = new Random();
        int p;

        if (kind == 0)
        {
            for (p = 0; p < 5 + random.nextInt(1); ++p)
            {
                this.setInventorySlotContents(random.nextInt(this.getSizeInventory()), AetherLoot.BRONZE.getRandomItem(random));
            }
        }

        if (kind == 1)
        {
            for (p = 0; p < 5 + random.nextInt(1); ++p)
            {
                this.setInventorySlotContents(random.nextInt(this.getSizeInventory()), AetherLoot.SILVER.getRandomItem(random));
            }
        }

        if (kind == 2)
        {
            for (p = 0; p < 5 + random.nextInt(1); ++p)
            {
                this.setInventorySlotContents(random.nextInt(this.getSizeInventory()), AetherLoot.GOLD.getRandomItem(random));
            }
        }

        this.locked = false;

        if (!this.worldObj.isRemote)
        {
            this.sendToAllInOurWorld(this.getDescriptionPacket());
        }
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        this.readFromNBT(pkt.customParam1);
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

    private void sendToAllInOurWorld(Packet pkt)
    {
        ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
        Iterator i$ = scm.playerEntityList.iterator();

        while (i$.hasNext())
        {
            Object obj = i$.next();
            EntityPlayerMP player = (EntityPlayerMP)obj;

            if (player.worldObj == this.worldObj)
            {
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
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
