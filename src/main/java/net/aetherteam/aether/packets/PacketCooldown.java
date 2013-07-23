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

public class PacketCooldown extends AetherPacket
{
    public PacketCooldown(int var1)
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
            int var10 = var3.readInt();
            String var11 = var3.readUTF();
            HashMap var12 = Aether.proxy.getClientCooldown();
            HashMap var13 = Aether.proxy.getClientMaxCooldown();
            HashMap var14 = Aether.proxy.getClientCooldownName();

            if (var6)
            {
                var12.clear();
                var13.clear();
                var14.clear();
            }

            for (int var15 = 0; var15 < var8; ++var15)
            {
                String var16 = var3.readUTF();

                if (var7)
                {
                    var12.put(var16, Integer.valueOf(var9));
                    var13.put(var16, Integer.valueOf(var10));
                    var14.put(var16, var11);
                    Aether.getClientPlayer((EntityPlayerSP)var2).updateGeneralCooldown();
                }
                else
                {
                    var12.remove(var16);
                    var13.remove(var16);
                    var14.remove(var16);
                }
            }
        }
        catch (Exception var17)
        {
            var17.printStackTrace();
        }
    }
}
