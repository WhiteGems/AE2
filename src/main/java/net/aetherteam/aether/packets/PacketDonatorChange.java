package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDonatorChange extends AetherPacket
{
    public PacketDonatorChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            byte packetType = dat.readByte();
            String username = dat.readUTF();
            String RSA = dat.readUTF();
            Aether.getInstance();
            Aether.syncDonatorList.addDonator(username, new Donator(username, RSA));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

