package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityBronzeDoorController extends TileEntity
{
    private Random rand = new Random();
    public static int keysRequired = 5;
    public int chatTime;
    public ArrayList keyList = new ArrayList();
    private int dungeonID = -1;

    public void setDungeonID(int var1)
    {
        this.dungeonID = var1;
    }

    public Dungeon getDungeon()
    {
        return DungeonHandler.instance().getDungeon(this.dungeonID);
    }

    public boolean hasDungeon()
    {
        return this.dungeonID != -1;
    }

    public void chatItUp(EntityPlayer var1, String var2)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(var1, var2);
            this.chatTime = 100;
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);

        for (int var2 = 0; var2 < var1.getInteger("KeyAmount"); ++var2)
        {
            this.keyList.add(new DungeonKey(EnumKeyType.getTypeFromString(var1.getString("Key" + var2))));
        }

        this.dungeonID = var1.getInteger("dungeonId");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("KeyAmount", this.keyList.size());

        for (int var2 = 0; var2 < this.keyList.size(); ++var2)
        {
            var1.setString("Key" + var2, ((DungeonKey)this.keyList.get(var2)).getType().name());
        }

        var1.setInteger("dungeonId", this.dungeonID);
    }

    public void addKey(DungeonKey var1)
    {
        if (var1 != null && (this.keyList != null || this.keyList.size() < keysRequired))
        {
            this.keyList.add(var1);
            this.onInventoryChanged();

            if (this.getDungeon() != null)
            {
                DungeonHandler.instance().removeKey(this.getDungeon(), this.getDungeon().getQueuedParty(), var1);
            }

            if (!this.worldObj.isRemote)
            {
                this.sendToAllInOurWorld(this.getDescriptionPacket());
            }
        }
    }

    public void addKeys(ArrayList var1)
    {
        if (var1 != null && var1.size() > 0 && (this.keyList != null || this.keyList.size() < keysRequired))
        {
            Iterator var2 = var1.iterator();

            while (var2.hasNext())
            {
                Object var3 = var2.next();

                if (this.keyList.size() < keysRequired && var3 instanceof DungeonKey)
                {
                    DungeonKey var4 = (DungeonKey)var3;
                    this.keyList.add(var4);
                }
            }

            Side var5 = FMLCommonHandler.instance().getEffectiveSide();

            if (this.getDungeon() != null)
            {
                for (int var6 = 0; var6 < var1.size(); ++var6)
                {
                    PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.removeDungeonKey(this.getDungeon(), this.getDungeon().getQueuedParty(), ((DungeonKey)var1.get(var6)).getType(), this));
                }

                this.getDungeon().getKeys().clear();
            }

            this.onInventoryChanged();

            if (!this.worldObj.isRemote)
            {
                this.sendToAllInOurWorld(this.getDescriptionPacket());
            }
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        Side var1 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.keyList != null && this.keyList.size() >= keysRequired && var1.isServer())
        {
            this.unlockDoor();
        }

        if (this.chatTime > 0)
        {
            --this.chatTime;
        }

        if (this.dungeonID == -1 && var1.isServer())
        {
            Dungeon var2 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)this.xCoord), MathHelper.floor_double((double)this.yCoord), MathHelper.floor_double((double)this.zCoord));

            if (var2 != null)
            {
                this.dungeonID = var2.getID();

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }
            }
        }
    }

    public void teleportMembersFromParty(ArrayList var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2.isServer())
        {
            MinecraftServer var3 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var4 = var3.getConfigurationManager();

            for (int var5 = 0; var5 < var4.playerEntityList.size(); ++var5)
            {
                Object var6 = var4.playerEntityList.get(var5);
                Iterator var7 = var1.iterator();

                while (var7.hasNext())
                {
                    PartyMember var8 = (PartyMember)var7.next();

                    if (var6 instanceof EntityPlayerMP && ((EntityPlayerMP)var6).username.equalsIgnoreCase(var8.username))
                    {
                        ((EntityPlayerMP)var6).setPositionAndUpdate((double)((float)((double)this.xCoord + 0.5D)), (double)((float)((double)this.yCoord + 1.0D)), (double)((float)((double)this.zCoord + 0.5D)));
                    }
                }
            }
        }

        this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.awake", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.unlock", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
    }

    public void unlockDoor()
    {
        Side var1 = FMLCommonHandler.instance().getEffectiveSide();

        for (int var2 = this.xCoord - 6; var2 < this.xCoord + 6; ++var2)
        {
            for (int var3 = this.yCoord - 6; var3 < this.yCoord + 6; ++var3)
            {
                for (int var4 = this.zCoord - 6; var4 < this.zCoord + 6; ++var4)
                {
                    if (this.worldObj.getBlockId(var2, var3, var4) == AetherBlocks.BronzeDoor.blockID && var1.isServer())
                    {
                        this.worldObj.setBlockToAir(var2, var3, var4);
                    }
                }
            }
        }

        this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.unlock", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

        if (var1.isServer())
        {
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, AetherBlocks.LockedDungeonStone.blockID);
        }
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public int getKeyAmount()
    {
        return this.keyList.size();
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
            EntityPlayerMP var5 = (EntityPlayerMP)var4;

            if (var5.worldObj == this.worldObj)
            {
                var5.playerNetServerHandler.sendPacketToPlayer(var1);
            }
        }
    }
}
