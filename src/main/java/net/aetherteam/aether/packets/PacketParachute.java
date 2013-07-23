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
import net.aetherteam.aether.CommonProxy;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketParachute extends AetherPacket
{
    public PacketParachute(int packetID)
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
            short length = dat.readShort();
            boolean parachuting = dat.readBoolean();
            int parachuteType = dat.readInt();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            HashMap playerParachuting = Aether.proxy.getClientParachuting();
            HashMap playerParachutingType = Aether.proxy.getClientParachuteType();

            if (clearFirst)
            {
                playerParachuting.clear();
            }

            for (int i = 0; i < length; i++)
            {
                String username = dat.readUTF();

                if (adding)
                {
                    playerParachuting.put(username, Boolean.valueOf(parachuting));
                    playerParachutingType.put(username, Integer.valueOf(parachuteType));
                }
                else
                {
                    playerParachuting.remove(username);
                    playerParachutingType.remove(username);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

