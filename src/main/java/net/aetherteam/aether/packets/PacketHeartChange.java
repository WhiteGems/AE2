package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHeartChange extends AetherPacket
{
    public PacketHeartChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            byte packetType = dat.readByte();
            boolean clearFirst = dat.readBoolean();
            boolean adding = dat.readBoolean();
            short length = dat.readShort();
            int maxHealth = dat.readInt();
            HashMap extraHearts = Aether.proxy.getClientExtraHearts();

            if (clearFirst)
            {
                extraHearts.clear();
            }

            for (int i = 0; i < length; i++)
            {
                String username = dat.readUTF();

                if (adding)
                {
                    extraHearts.put(username, Integer.valueOf(maxHealth));
                }
                else
                {
                    extraHearts.remove(username);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

