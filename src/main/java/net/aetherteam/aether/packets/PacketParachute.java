package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketParachute extends AetherPacket
{
    public PacketParachute(int var1)
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
            short var8 = var3.readShort();
            boolean var9 = var3.readBoolean();
            int var10 = var3.readInt();
            Side var11 = FMLCommonHandler.instance().getEffectiveSide();
            HashMap var12 = Aether.proxy.getClientParachuting();
            HashMap var13 = Aether.proxy.getClientParachuteType();

            if (var6)
            {
                var12.clear();
            }

            for (int var14 = 0; var14 < var8; ++var14)
            {
                String var15 = var3.readUTF();

                if (var7)
                {
                    var12.put(var15, Boolean.valueOf(var9));
                    var13.put(var15, Integer.valueOf(var10));
                }
                else
                {
                    var12.remove(var15);
                    var13.remove(var15);
                }
            }
        }
        catch (Exception var16)
        {
            var16.printStackTrace();
        }
    }
}
