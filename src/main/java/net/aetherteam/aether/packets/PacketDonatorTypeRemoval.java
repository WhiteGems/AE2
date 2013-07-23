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
    public PacketDonatorTypeRemoval(int var1)
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
            byte var7 = var3.readByte();

            if (var7 >= 1)
            {
                EnumChoiceType var8 = EnumChoiceType.getTypeFromString(var6);

                if (var8 == null)
                {
                    System.out.println("Choice type was null! Packet handling unsuccessful.");
                    return;
                }

                System.out.println("Choice type transferred!");
                Aether.getInstance();

                if (Aether.syncDonatorList.getDonator(var5) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.getDonator(var5).removeChoiceType(var8);
                }
            }
            else
            {
                Aether var10000 = Aether.instance;
                SyncDonatorList var10 = Aether.syncDonatorList;
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorTypeRemoval(var5, EnumChoiceType.getTypeFromString(var6), (byte)1));
                System.out.println("Server received \'Remove Type\' packet, dispatching to players!");
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }
    }
}
