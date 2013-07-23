package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPlayerClientInfo extends AetherPacket
{
    public PacketPlayerClientInfo(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));
        new BufferedReader(new InputStreamReader(var3));

        try
        {
            byte var5 = var3.readByte();
            boolean var6 = var3.readBoolean();
            boolean var7 = var3.readBoolean();
            String var8 = var3.readUTF();
            short var9 = var3.readShort();
            short var10 = var3.readShort();
            short var11 = var3.readShort();
            short var12 = var3.readShort();
            int var13 = var3.readInt();
            HashMap var14 = Aether.proxy.getPlayerClientInfo();

            if (var6)
            {
                var14.clear();
            }

            if (var7)
            {
                PlayerClientInfo var15 = new PlayerClientInfo(var9, var10, var11, var12, var13);
                var14.put(var8, var15);
            }
            else
            {
                var14.remove(var8);
            }
        }
        catch (Exception var16)
        {
            var16.printStackTrace();
        }
    }
}
