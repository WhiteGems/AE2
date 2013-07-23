package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHeartChange extends AetherPacket
{
    public PacketHeartChange(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));

        try
        {
            byte var4 = var3.readByte();
            boolean var5 = var3.readBoolean();
            boolean var6 = var3.readBoolean();
            short var7 = var3.readShort();
            int var8 = var3.readInt();
            HashMap var9 = Aether.proxy.getClientExtraHearts();

            if (var5)
            {
                var9.clear();
            }

            for (int var10 = 0; var10 < var7; ++var10)
            {
                String var11 = var3.readUTF();

                if (var6)
                {
                    var9.put(var11, Integer.valueOf(var8));
                }
                else
                {
                    var9.remove(var11);
                }
            }
        }
        catch (Exception var12)
        {
            var12.printStackTrace();
        }
    }
}
