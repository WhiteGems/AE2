package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketCoinChange extends AetherPacket
{
    public PacketCoinChange(int var1)
    {
        super(var1);
    }

    @SideOnly(Side.CLIENT)
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
            int var9 = var3.readInt();
            HashMap var10 = Aether.proxy.getClientCoins();

            if (var6)
            {
                var10.clear();
            }

            for (int var11 = 0; var11 < var8; ++var11)
            {
                String var12 = var3.readUTF();

                if (var7)
                {
                    var10.put(var12, Integer.valueOf(var9));
                    Aether.getClientPlayer((EntityPlayerSP)var2).updateCoinAmount();
                }
                else
                {
                    var10.remove(var12);
                }
            }
        }
        catch (Exception var13)
        {
            var13.printStackTrace();
        }
    }
}
