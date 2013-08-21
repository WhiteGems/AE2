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
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonMemberQueue extends AetherPacket
{
    public PacketDungeonMemberQueue(int packetID)
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
            PartyMember potentialMember;
            Party party;

            if (side.isClient())
            {
                potentialMember = PartyController.instance().getMember(memberName);
                party = PartyController.instance().getParty(potentialMember);
                Dungeon entityPlayer = DungeonHandler.instance().getDungeon(dungeonID);

                if (party != null && entityPlayer != null)
                {
                    DungeonHandler.instance().queueMember(entityPlayer, potentialMember, false);
                }
            }
            else
            {
                potentialMember = PartyController.instance().getMember(memberName);
                party = PartyController.instance().getParty(potentialMember);
                EntityPlayerMP entityPlayer1 = (EntityPlayerMP)player;
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if (party != null && dungeon != null)
                {
                    if (entityPlayer1.username.equalsIgnoreCase(memberName))
                    {
                        System.out.println("No validation needed, adding party member \'" + memberName + "\' to the Dungeon.");
                        DungeonHandler.instance().queueMember(dungeon, potentialMember, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonMemberQueue(dungeon, potentialMember), player);
                    }
                    else
                    {
                        System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed from Dungeon Instance.");
                    }
                }
            }
        }
        catch (Exception var13)
        {
            var13.printStackTrace();
        }
    }
}
