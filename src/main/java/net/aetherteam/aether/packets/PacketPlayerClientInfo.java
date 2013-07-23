package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPlayerClientInfo extends AetherPacket
{
    public PacketPlayerClientInfo(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        BufferedReader buf = new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte packetType = dat.readByte();
            boolean clearFirst = dat.readBoolean();
            boolean adding = dat.readBoolean();
            String username = dat.readUTF();
            short halfHearts = dat.readShort();
            short maxHealth = dat.readShort();
            short hunger = dat.readShort();
            short armourValue = dat.readShort();
            int aetherCoins = dat.readInt();
            HashMap playerClientInfo = Aether.proxy.getPlayerClientInfo();

            if (clearFirst)
            {
                playerClientInfo.clear();
            }

            if (adding)
            {
                PlayerClientInfo playerInfo = new PlayerClientInfo(halfHearts, maxHealth, hunger, armourValue, aetherCoins);
                playerClientInfo.put(username, playerInfo);
            }
            else
            {
                playerClientInfo.remove(username);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

