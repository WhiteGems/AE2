package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.mounts_old.Ridable;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketRiding extends AetherPacket
{
    public PacketRiding(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));
        int var4 = -1;

        try
        {
            byte var5 = var3.readByte();
            var4 = var3.readInt();
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Ridable var7 = null;

        if (var4 != -1)
        {
            var7 = (Ridable)Minecraft.getMinecraft().theWorld.getEntityByID(var4);
        }

        if (var7 != null)
        {
            var7.getRidingHandler().setRider((EntityPlayer)var2);
        }
        else
        {
            Aether.getPlayerBase((EntityPlayer)var2).rideEntity((Entity)null, (RidingHandler)null);
        }
    }
}
