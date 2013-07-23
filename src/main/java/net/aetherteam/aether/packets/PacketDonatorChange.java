package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.donator.Donator;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDonatorChange extends AetherPacket
{
    public PacketDonatorChange(int var1)
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
            Aether.getInstance();
            Aether.syncDonatorList.addDonator(var5, new Donator(var5, var6));
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }
    }
}
