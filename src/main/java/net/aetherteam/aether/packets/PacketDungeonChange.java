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
    public PacketDungeonChange(int packetID)
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
            boolean adding = dat.readBoolean();

            int dungeonID = dat.readInt();

            DungeonType dungeonType = null;
            int x = -1;
            int z = -1;
            int amountBoxes = 0;
            StructureBoundingBoxSerial boundingBox = null;
            ArrayList boundingBoxes = null;

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (adding)
            {
                dungeonType = DungeonType.getTypeFromString(dat.readUTF());
                x = dat.readInt();
                z = dat.readInt();

                amountBoxes = dat.readShort();

                boundingBox = new StructureBoundingBoxSerial(dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt());
                boundingBoxes = new ArrayList();

                for (int i = 0; i < amountBoxes; i++)
                {
                    boundingBoxes.add(new StructureBoundingBoxSerial(dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt()));
                }
            }

            if (side.isClient())
            {
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if (adding)
                {
                    if (dungeon != null)
                    {
                        DungeonHandler.instance().removeInstance(dungeon);
                    }

                    DungeonHandler.instance().addInstance(new Dungeon(dungeonType, x, z, boundingBox, boundingBoxes));
                } else if (dungeon != null)
                {
                    DungeonHandler.instance().removeInstance(dungeon);
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketDungeonChange
 * JD-Core Version:    0.6.2
 */