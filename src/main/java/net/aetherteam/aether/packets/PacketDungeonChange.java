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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            boolean adding = dat.readBoolean();
            int dungeonID = dat.readInt();
            DungeonType dungeonType = null;
            int x = -1;
            int z = -1;
            boolean amountBoxes = false;
            StructureBoundingBoxSerial boundingBox = null;
            ArrayList boundingBoxes = null;
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (adding)
            {
                dungeonType = DungeonType.getTypeFromString(dat.readUTF());
                x = dat.readInt();
                z = dat.readInt();
                short var17 = dat.readShort();
                boundingBox = new StructureBoundingBoxSerial(dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt());
                boundingBoxes = new ArrayList();

                for (int dungeon = 0; dungeon < var17; ++dungeon)
                {
                    boundingBoxes.add(new StructureBoundingBoxSerial(dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt()));
                }
            }

            if (side.isClient())
            {
                Dungeon var18 = DungeonHandler.instance().getDungeon(dungeonID);

                if (adding)
                {
                    if (var18 != null)
                    {
                        DungeonHandler.instance().removeInstance(var18);
                    }

                    DungeonHandler.instance().addInstance(new Dungeon(dungeonType, x, z, boundingBox, boundingBoxes));
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
