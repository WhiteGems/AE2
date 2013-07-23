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
    public PacketDonatorChoice(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));

        try
        {
            byte var4 = var3.readByte();
            String var5 = var3.readUTF();
            String var6 = var3.readUTF();
            boolean var7 = var3.readBoolean();
            byte var8 = var3.readByte();

            if (var8 >= 1)
            {
                DonatorChoice var9 = DonatorChoice.getChoiceFromString(var6);

                if (var9 == null)
                {
                    System.out.println("Choice was null! Packet handling unsuccessful.");
                    return;
                }

                System.out.println("Choice transferred!");

                if (var7)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(var5).addChoice(var9);
                }
                else
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(var5).removeChoiceType(var9.type);
                }
            }
            else
            {
                Aether var10000 = Aether.instance;
                SyncDonatorList var11 = Aether.syncDonatorList;
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorChoice(var5, DonatorChoice.getChoiceFromString(var6), var7, (byte)1));
                System.out.println("Server received packet, dispatching to players!");
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }
    }
}
