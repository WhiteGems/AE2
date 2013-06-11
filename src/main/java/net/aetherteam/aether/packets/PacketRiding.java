package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.entities.mounts_old.Ridable;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketRiding extends AetherPacket
{
    public PacketRiding(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        int id = -1;
        try
        {
            byte packetType = dat.readByte();
            id = dat.readInt();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Ridable entity = null;

        if (id != -1)
        {
            entity = (Ridable) Minecraft.getMinecraft().theWorld.getEntityByID(id);
        }

        if (entity != null) entity.getRidingHandler().setRider((EntityPlayer) player);
        else Aether.getPlayerBase((EntityPlayer) player).rideEntity(null, null);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketRiding
 * JD-Core Version:    0.6.2
 */