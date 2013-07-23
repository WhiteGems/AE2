package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.entities.mounts.MountInput;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPlayerInput extends AetherPacket
{
    public PacketPlayerInput(int var1)
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
            String var6 = var3.readUTF();
            ArrayList var7 = new ArrayList();
            int var8 = var3.readInt();

            for (int var9 = 0; var9 < var8; ++var9)
            {
                MountInput var10 = MountInput.getInputFromString(var3.readUTF());
                var7.add(var10);
            }

            PlayerBaseAetherServer var12 = Aether.getServerPlayer((EntityPlayerMP)var2);
            var12.mountInput = var7;
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
