package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.Player;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerAetherServer;
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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            String username = dat.readUTF();
            ArrayList mountInput = new ArrayList();
            int inputSize = dat.readInt();

            for (int playerBase = 0; playerBase < inputSize; ++playerBase)
            {
                MountInput direction = MountInput.getInputFromString(dat.readUTF());
                mountInput.add(direction);
            }

            PlayerAetherServer var12 = Aether.getServerPlayer((EntityPlayerMP)player);
            var12.mountInput = mountInput;
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
