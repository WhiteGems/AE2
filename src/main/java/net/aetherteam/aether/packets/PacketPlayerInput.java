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
    public PacketPlayerInput(int packetID)
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
            String username = dat.readUTF();

            ArrayList mountInput = new ArrayList();

            int inputSize = dat.readInt();

            for (int count = 0; count < inputSize; count++)
            {
                MountInput direction = MountInput.getInputFromString(dat.readUTF());

                mountInput.add(direction);
            }

            PlayerBaseAetherServer playerBase = Aether.getServerPlayer((EntityPlayerMP) player);

            playerBase.mountInput = mountInput;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketPlayerInput
 * JD-Core Version:    0.6.2
 */