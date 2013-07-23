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

public class PacketCoinChange extends AetherPacket
{
    public PacketCoinChange(int packetID)
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
            int coinAmount = dat.readInt();
            HashMap playerCoins = Aether.proxy.getClientCoins();

            if (clearFirst)
            {
                playerCoins.clear();
            }

            for (int i = 0; i < length; i++)
            {
                String username = dat.readUTF();

                if (adding)
                {
                    playerCoins.put(username, Integer.valueOf(coinAmount));
                    Aether.getClientPlayer((MovementInputFromOptions)player).updateCoinAmount();
                }
                else
                {
                    playerCoins.remove(username);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

