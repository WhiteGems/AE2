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
import net.minecraft.client.audio.SoundManager;
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

    public void chatItUp(EntityPlayer var1, String var2)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(var1, var2);
            this.chatTime = 100;
        }
    }

    public void setDungeonID(int var1)
    {
        this.dungeonID = var1;
    }

    public Dungeon getDungeon()
    {
        return DungeonHandler.instance().getDungeon(this.dungeonID);
    }

    public boolean isSoundOn()
    {
        boolean var1;

        if (this.isClient() && Aether.proxy.getClient().sndManager != null)
        {
            SoundManager var10000 = Aether.proxy.getClient().sndManager;

            if (SoundManager.sndSystem != null)
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        SoundManager var10000 = Aether.proxy.getClient().sndManager;
        boolean var1;

        if (SoundManager.sndSystem != null)
        {
            var10000 = Aether.proxy.getClient().sndManager;

            if (SoundManager.sndSystem.playing("streaming"))
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            SoundManager var10000 = Aether.proxy.getClient().sndManager;
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void playMusicFile(String var1)
    {
        if (this.isSoundOn())
        {
            float var10002 = (float)this.xCoord;
            float var10003 = (float)this.yCoord;
            Aether.proxy.getClient().sndManager.playStreaming(var1, var10002, var10003, (float)this.zCoord);
        }
    }

    public void teleportMembersFromParty(ArrayList var1, boolean var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        if (var3.isServer())
        {
            MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var5 = var4.getConfigurationManager();

            for (int var6 = 0; var6 < var5.playerEntityList.size(); ++var6)
            {
                Object var7 = var5.playerEntityList.get(var6);
                Iterator var8 = var1.iterator();

                while (var8.hasNext())
                {
                    PartyMember var9 = (PartyMember)var8.next();

                    if (var7 instanceof EntityPlayerMP && ((EntityPlayerMP)var7).username.equalsIgnoreCase(var9.username))
                    {
                        ((EntityPlayerMP)var7).setPositionAndUpdate((double)((float)((double)this.xCoord + 0.5D)), (double)((float)((double)this.yCoord + 1.0D)), (double)((float)((double)this.zCoord + 0.5D + (var2 ? 3.0D : 0.0D))));
                    }
                }
            }
        }

        if (!var2)
        {
            this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.awake", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.worldObj.playSoundEffect((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, "aeboss.slider.unlock", 1.0F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.dungeonID = var1.getInteger("dungeonId");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("dungeonId", this.dungeonID);
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
            Dungeon var1 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)this.xCoord), MathHelper.floor_double((double)this.yCoord), MathHelper.floor_double((double)this.zCoord));

            if (var1 != null)
            {
                this.dungeonID = var1.getID();

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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

    public boolean hasDungeon()
    {
        return this.dungeonID != -1;
    }
}
