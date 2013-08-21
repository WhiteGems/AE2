package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDonatorChoice extends AetherPacket
{
    public PacketDonatorChoice(int packetID)
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
            String choiceName = dat.readUTF();
            boolean adding = dat.readBoolean();
            byte proxy = dat.readByte();

            if (proxy >= 1)
            {
                DonatorChoice donators = DonatorChoice.getChoiceFromString(choiceName);

                if (donators == null)
                {
                    System.out.println("Choice was null! Packet handling unsuccessful.");
                    return;
                }

                System.out.println("Choice transferred!");

                if (adding)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(username).addChoice(donators);
                }
                else
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(username).removeChoiceType(donators.type);
                }
            }
            else
            {
                Aether var10000 = Aether.instance;
                SyncDonatorList donators1 = Aether.syncDonatorList;
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorChoice(username, DonatorChoice.getChoiceFromString(choiceName), adding, (byte)1));
                System.out.println("Server received packet, dispatching to players!");
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }
    }
}
