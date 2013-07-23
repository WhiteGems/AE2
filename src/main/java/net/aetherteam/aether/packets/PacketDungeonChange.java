package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.DungeonType;
import net.aetherteam.aether.worldgen.StructureBoundingBoxSerial;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonChange extends AetherPacket
{
    public PacketDungeonChange(int var1)
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
            boolean var6 = var3.readBoolean();
            int var7 = var3.readInt();
            DungeonType var8 = null;
            int var9 = -1;
            int var10 = -1;
            boolean var11 = false;
            StructureBoundingBoxSerial var12 = null;
            ArrayList var13 = null;
            Side var14 = FMLCommonHandler.instance().getEffectiveSide();

            if (var6)
            {
                var8 = DungeonType.getTypeFromString(var3.readUTF());
                var9 = var3.readInt();
                var10 = var3.readInt();
                short var17 = var3.readShort();
                var12 = new StructureBoundingBoxSerial(var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt());
                var13 = new ArrayList();

                for (int var15 = 0; var15 < var17; ++var15)
                {
                    var13.add(new StructureBoundingBoxSerial(var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt(), var3.readInt()));
                }
            }

            if (var14.isClient())
            {
                Dungeon var18 = DungeonHandler.instance().getDungeon(var7);

                if (var6)
                {
                    if (var18 != null)
                    {
                        DungeonHandler.instance().removeInstance(var18);
                    }

                    DungeonHandler.instance().addInstance(new Dungeon(var8, var9, var10, var12, var13));
                }
                else if (var18 != null)
                {
                    DungeonHandler.instance().removeInstance(var18);
                }
            }
        }
        catch (Exception var16)
        {
            var16.printStackTrace();
        }
    }
}
