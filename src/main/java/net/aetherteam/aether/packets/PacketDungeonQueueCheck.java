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
    public PacketDungeonQueueCheck(int var1)
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
            int var6 = var3.readInt();
            Side var7 = FMLCommonHandler.instance().getEffectiveSide();

            if (var7.isClient())
            {
                Dungeon var8 = DungeonHandler.instance().getDungeon(var6);

                if (var8 != null)
                {
                    DungeonHandler.instance().checkForQueue(var8);
                }
            }
        }
        catch (Exception var9)
        {
            var9.printStackTrace();
        }
    }
}
