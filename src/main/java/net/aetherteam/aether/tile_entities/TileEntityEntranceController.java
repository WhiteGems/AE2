package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
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

public class TileEntityEntranceController extends TileEntity
{
    private Random rand = new Random();
    public int chatTime;
    private int dungeonID = -1;

    public void chatItUp(EntityPlayer player, String s)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(player, s);
            this.chatTime = 100;
        }
    }

    public void setDungeonID(int id)
    {
        this.dungeonID = id;
    }

    public Dungeon getDungeon()
    {
        return DungeonHandler.instance().getDungeon(this.dungeonID);
    }

    public boolean isSoundOn()
    {
        return this.isClient() && Aether.proxy.getClient().sndManager != null && Aether.proxy.getClient().sndManager.sndSystem != null;
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return Aether.proxy.getClient().sndManager.sndSystem != null && Aether.proxy.getClient().sndManager.sndSystem.playing("streaming");
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            Aether.proxy.getClient().sndManager.sndSystem.stop("streaming");
        }
    }

    public void playMusicFile(String fileName)
    {
        if (this.isSoundOn())
        {
            float var10002 = (float)this.xCoord;
            float var10003 = (float)this.yCoord;
            Aether.proxy.getClient().sndManager.playStreaming(fileName, var10002, var10003, (float)this.zCoord);
        }
    }

    public void teleportMembersFromParty(ArrayList<PartyMember> members, boolean out)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); ++playerAmount)
            {
                Object player = configManager.playerEntityList.get(playerAmount);
                Iterator i$ = members.iterator();

                while (i$.hasNext())
                {
                    PartyMember member = (PartyMember)i$.next();

                    if (player instanceof EntityPlayerMP && ((EntityPlayerMP)player).username.equalsIgnoreCase(member.username))
                    {
                        ((EntityPlayerMP)player).setPositionAndUpdate((double)((float)((double)this.xCoord + 0.5D)), (double)((float)((double)this.yCoord + 1.0D)), (double)((float)((double)this.zCoord + 0.5D + (out ? 3.0D : 0.0D))));
                    }
                }
            }
        }

        if (!out)
        {
            this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aether:aeboss.slider.awake", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aether:aeboss.slider.unlock", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.dungeonID = nbttagcompound.getInteger("dungeonId");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("dungeonId", this.dungeonID);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (this.chatTime > 0)
        {
            --this.chatTime;
        }

        if (this.dungeonID == -1)
        {
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)this.xCoord), MathHelper.floor_double((double)this.yCoord), MathHelper.floor_double((double)this.zCoord));

            if (dungeon != null)
            {
                this.dungeonID = dungeon.getID();

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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

    public boolean hasDungeon()
    {
        return this.dungeonID != -1;
    }
}
