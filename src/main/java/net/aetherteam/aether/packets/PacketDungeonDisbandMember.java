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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            int dungeonID = dat.readInt();
            String memberName = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Dungeon leavingMember = DungeonHandler.instance().getDungeon(dungeonID);
                PartyMember actualMember = PartyController.instance().getMember(memberName);

                if (leavingMember != null && actualMember != null)
                {
                    DungeonHandler.instance().disbandMember(leavingMember, actualMember, false);
                }
            }
            else
            {
                PartyMember leavingMember1 = PartyController.instance().getMember(memberName);
                EntityPlayer actualMember1 = (EntityPlayer)player;
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if (dungeon != null && dungeon.isActive() && leavingMember1 != null && leavingMember1.username.equalsIgnoreCase(actualMember1.username))
                {
                    DungeonHandler.instance().disbandMember(dungeon, leavingMember1, false);
                    this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonDisbandMember(dungeon, leavingMember1), player);
                }
            }
        }
        catch (Exception var12)
        {
            var12.printStackTrace();
        }
    }
}
