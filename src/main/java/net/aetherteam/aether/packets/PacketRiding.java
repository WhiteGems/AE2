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
            byte entity = dat.readByte();
            id = dat.readInt();
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Ridable entity1 = null;

        if (id != -1)
        {
            entity1 = (Ridable)Minecraft.getMinecraft().theWorld.getEntityByID(id);
        }

        if (entity1 != null)
        {
            entity1.getRidingHandler().setRider((EntityPlayer)player);
        }
        else
        {
            Aether.getPlayerBase((EntityPlayer)player).rideEntity((Entity)null, (RidingHandler)null);
        }
    }
}
