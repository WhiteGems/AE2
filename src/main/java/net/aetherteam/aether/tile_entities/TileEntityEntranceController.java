package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
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
import paulscode.sound.SoundSystem;

public class TileEntityEntranceController extends TileEntity
{
    private Random rand;
    public int chatTime;
    private int dungeonID = -1;

    public TileEntityEntranceController()
    {
        this.rand = new Random();
    }

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
        return (isClient()) && (Aether.proxy.getClient().sndManager != null) && (SoundPoolEntry.soundName != null);
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return (SoundPoolEntry.soundName != null) && (SoundPoolEntry.soundName.playing("streaming"));
    }

    public void turnMusicOff()
    {
        if (isSoundOn())
        {
            SoundPoolEntry.soundName.stop("streaming");
        }
    }

    public void playMusicFile(String fileName)
    {
        if (isSoundOn())
        {
            Aether.proxy.getClient().sndManager.a(fileName, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void teleportMembersFromParty(ArrayList members, boolean out)
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
                        ((EntityPlayerMP)player).setPositionAndUpdate((float)(this.xCoord + 0.5D), (float)(this.yCoord + 1.0D), (float)(this.zCoord + 0.5D + (out ? 3.0D : 0.0D)));
                    }
                }
            }
        }

        if (!out)
        {
            this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.awake", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "aeboss.slider.unlock", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.dungeonID = nbttagcompound.getInteger("dungeonId");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("dungeonId", this.dungeonID);
    }

    public void updateEntity()
    {
        if (this.chatTime > 0)
        {
            this.chatTime -= 1;
        }

        if (this.dungeonID == -1)
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

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
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

    public boolean hasDungeon()
    {
        return this.dungeonID != -1;
    }
}

