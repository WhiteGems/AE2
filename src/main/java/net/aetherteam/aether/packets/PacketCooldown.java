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
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MovementInputFromOptions;

public class PacketCooldown extends AetherPacket
{
    public PacketCooldown(int packetID)
    {
        super(packetID);
    }

    @SideOnly(Side.CLIENT)
    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        BufferedReader buf = new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte packetType = dat.readByte();
            boolean clearFirst = dat.readBoolean();
            boolean adding = dat.readBoolean();
            short length = dat.readShort();
            int cooldown = dat.readInt();
            int cooldownMax = dat.readInt();
            String stackName = dat.readUTF();
            HashMap playerCooldowns = Aether.proxy.getClientCooldown();
            HashMap playerMaxCooldowns = Aether.proxy.getClientMaxCooldown();
            HashMap playerCooldownName = Aether.proxy.getClientCooldownName();

            if (clearFirst)
            {
                playerCooldowns.clear();
                playerMaxCooldowns.clear();
                playerCooldownName.clear();
            }

            for (int i = 0; i < length; i++)
            {
                String username = dat.readUTF();

                if (adding)
                {
                    playerCooldowns.put(username, Integer.valueOf(cooldown));
                    playerMaxCooldowns.put(username, Integer.valueOf(cooldownMax));
                    playerCooldownName.put(username, stackName);
                    Aether.getClientPlayer((MovementInputFromOptions)player).updateGeneralCooldown();
                }
                else
                {
                    playerCooldowns.remove(username);
                    playerMaxCooldowns.remove(username);
                    playerCooldownName.remove(username);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

