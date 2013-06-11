package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityBronzeDoorController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketDungeonKey extends AetherPacket
{
    public PacketDungeonKey(int packetID)
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
            String partyName = dat.readUTF();

            DungeonKey key = new DungeonKey(EnumKeyType.getTypeFromString(dat.readUTF()));

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if ((party != null) && (dungeon != null) && (key != null))
                {
                    if (adding)
                    {
                        DungeonHandler.instance().addKey(dungeon, party, key);
                    } else DungeonHandler.instance().removeKey(dungeon, party, key);
                }
            } else if (!adding)
            {
                Party party = PartyController.instance().getParty(partyName);
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                EntityPlayer sendingPlayer = (EntityPlayer) player;

                int x = dat.readInt();
                int y = dat.readInt();
                int z = dat.readInt();

                TileEntityBronzeDoorController bronzeDoor = (TileEntityBronzeDoorController) sendingPlayer.worldObj.getBlockTileEntity(x, y, z);

                if ((party != null) && (dungeon != null) && (key != null))
                {
                    if (dungeon.getQueuedParty().hasMember(PartyController.instance().getMember(sendingPlayer)))
                    {
                        DungeonHandler.instance().removeKey(dungeon, party, key);

                        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.removeDungeonKey(dungeon, party, key.getType(), bronzeDoor));
                    }
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketDungeonKey
 * JD-Core Version:    0.6.2
 */