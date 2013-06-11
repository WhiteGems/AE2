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
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonDisbandMember extends AetherPacket
{
    public PacketDungeonDisbandMember(int packetID)
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
            String memberName = dat.readUTF();

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                PartyMember leavingMember = PartyController.instance().getMember(memberName);

                if ((dungeon != null) && (leavingMember != null))
                {
                    DungeonHandler.instance().disbandMember(dungeon, leavingMember, false);
                }
            } else
            {
                PartyMember leavingMember = PartyController.instance().getMember(memberName);

                EntityPlayer actualMember = (EntityPlayer) player;

                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if ((dungeon != null) && (dungeon.isActive()))
                {
                    if ((leavingMember != null) && (leavingMember.username.equalsIgnoreCase(actualMember.username)))
                    {
                        DungeonHandler.instance().disbandMember(dungeon, leavingMember, false);

                        sendPacketToAllExcept(AetherPacketHandler.sendDungeonDisbandMember(dungeon, leavingMember), player);
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
 * Qualified Name:     net.aetherteam.aether.packets.PacketDungeonDisbandMember
 * JD-Core Version:    0.6.2
 */