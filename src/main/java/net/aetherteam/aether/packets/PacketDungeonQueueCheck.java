package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonQueueCheck extends AetherPacket
{
    public PacketDungeonQueueCheck(int packetID)
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
            int dungeonID = dat.readInt();
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if (dungeon != null)
                {
                    DungeonHandler.instance().checkForQueue(dungeon);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

