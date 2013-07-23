package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityBronzeDoorController extends TileEntity
{
    private Random rand;
    public static int keysRequired = 5;
    public int chatTime;
    public ArrayList keyList = new ArrayList();

    private int dungeonID = -1;

    public TileEntityBronzeDoorController()
    {
        this.rand = new Random();
    }

    public void setDungeonID(int id)
    {
        this.dungeonID = id;
    }

    public Dungeon getDungeon()
    {
        return DungeonHandler.instance().getDungeon(this.dungeonID);
    }

    public boolean hasDungeon()
    {
        return this.dungeonID != -1;
    }

    public void chatItUp(EntityPlayer player, String s)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(player, s);
            this.chatTime = 100;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        for (int i = 0; i < nbttagcompound.getInteger("KeyAmount"); i++)
        {
            this.keyList.add(new DungeonKey(EnumKeyType.getTypeFromString(nbttagcompound.getString("Key" + i))));
        }

        this.dungeonID = nbttagcompound.getInteger("dungeonId");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("KeyAmount", this.keyList.size());

        for (int i = 0; i < this.keyList.size(); i++)
        {
            nbttagcompound.setString("Key" + i, ((DungeonKey)this.keyList.get(i)).getType().name());
        }

        nbttagcompound.setInteger("dungeonId", this.dungeonID);
    }

    public void addKey(DungeonKey key)
    {
        if ((key == null) || ((this.keyList == null) && (this.keyList.size() >= keysRequired)))
        {
            return;
        }

        this.keyList.add(key);
        onInventoryChanged();

        if (getDungeon() != null)
        {
            DungeonHandler.instance().removeKey(getDungeon(), getDungeon().getQueuedParty(), key);
        }

        if (!this.worldObj.isRemote)
        {
            sendToAllInOurWorld(getDescriptionPacket());
        }
    }

    public void addKeys(ArrayList keys)
    {
        if ((keys == null) || (keys.size() <= 0) || ((this.keyList == null) && (this.keyList.size() >= keysRequired)))
        {
            return;
        }

        Iterator it = keys.iterator();

        while (it.hasNext())
        {
            Object obj = it.next();

            if ((this.keyList.size() < keysRequired) && ((obj instanceof DungeonKey)))
            {
                DungeonKey key = (DungeonKey)obj;
                this.keyList.add(key);
            }
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (getDungeon() != null)
        {
            for (int i = 0; i < keys.size(); i++)
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.removeDungeonKey(getDungeon(), getDungeon().getQueuedParty(), ((DungeonKey)keys.get(i)).getType(), this));
            }

            getDungeon().getKeys().clear();
        }

        onInventoryChanged();

        if (!this.worldObj.isRemote)
        {
            sendToAllInOurWorld(getDescriptionPacket());
        }
    }

    public void updateEntity()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.keyList != null) && (this.keyList.size() >= keysRequired) && (side.isServer()))
        {
            unlockDoor();
        }

        if (this.chatTime > 0)
        {
            this.chatTime -= 1;
        }

        if ((this.dungeonID == -1) && (side.isServer()))
        {
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.xCoord), MathHelper.floor_double(this.yCoord), MathHelper.floor_double(this.zCoord));

            if (dungeon != null)
            {
                this.dungeonID = dungeon.getID();

                if (!this.worldObj.isRemote)
                {
                    sendToAllInOurWorld(getDescriptionPacket());
                }
            }
        }
    }

    public void teleportMembersFromParty(ArrayList members)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();
            Object player;

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); playerAmount++)
            {
                player = configManager.playerEntityList.get(playerAmount);

                for (PartyMember member : members)
                {
                    if (((player instanceof EntityPlayerMP)) && (((EntityPlayerMP)player).username.equalsIgnoreCase(member.username)))
                    {
                        ((EntityPlayerMP)player).setPositionAndUpdate((float)(this.xCoord + 0.5D), (float)(this.yCoord + 1.0D), (float)(this.zCoord + 0.5D));
                    }
                }
            }
        }

        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.awake", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.unlock", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
    }

    public void unlockDoor()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        for (int x = this.xCoord - 6; x < this.xCoord + 6; x++)
        {
            for (int y = this.yCoord - 6; y < this.yCoord + 6; y++)
            {
                for (int z = this.zCoord - 6; z < this.zCoord + 6; z++)
                {
                    if ((this.worldObj.getBlockId(x, y, z) == AetherBlocks.BronzeDoor.blockID) && (side.isServer()))
                    {
                        this.worldObj.setBlockToAir(x, y, z);
                    }
                }
            }
        }

        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.unlock", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

        if (side.isServer())
        {
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, AetherBlocks.LockedDungeonStone.blockID);
        }
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }
    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public int getKeyAmount()
    {
        return this.keyList.size();
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        readFromNBT(pkt.customParam1);
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    private void sendToAllInOurWorld(Packet pkt)
    {
        ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();

        for (Iterator i$ = scm.playerEntityList.iterator(); i$.hasNext();)
        {
            Object obj = i$.next();
            EntityPlayerMP player = (EntityPlayerMP)obj;

            if (player.worldObj == this.worldObj)
            {
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
            }
        }
    }
}

