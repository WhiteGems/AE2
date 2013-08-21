package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDonatorTypeRemoval extends AetherPacket
{
    public PacketDonatorTypeRemoval(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            byte e = dat.readByte();
            String username = dat.readUTF();
            String typeName = dat.readUTF();
            byte proxy = dat.readByte();

            if (proxy >= 1)
            {
                EnumChoiceType donators = EnumChoiceType.getTypeFromString(typeName);

                if (donators == null)
                {
                    System.out.println("Choice type was null! Packet handling unsuccessful.");
                    return;
                }

                System.out.println("Choice type transferred!");
                Aether.getInstance();

                if (Aether.syncDonatorList.getDonator(username) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(username).removeChoiceType(donators);
                }
            }
            else
            {
                Aether var10000 = Aether.instance;
                SyncDonatorList donators1 = Aether.syncDonatorList;
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorTypeRemoval(username, EnumChoiceType.getTypeFromString(typeName), (byte)1));
                System.out.println("Server received \'Remove Type\' packet, dispatching to players!");
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }
    }
}
