package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPartyMemberChange extends AetherPacket
{
    public PacketPartyMemberChange(int packetID)
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

            String partyName = dat.readUTF();
            String username = dat.readUTF();
            String skinURL = dat.readUTF();

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);

                if (party != null)
                {
                    if (adding)
                    {
                        PartyMember newMember = new PartyMember(username, skinURL);

                        PartyController.instance().joinParty(party, newMember, false);

                        System.out.println("Added Player '" + username + "' to the Party: " + partyName + "!");
                    } else
                    {
                        PartyMember partyMember = PartyController.instance().getMember(username);

                        PartyController.instance().leaveParty(party, partyMember, false);

                        System.out.println("Removed Player '" + username + "' from the Party: " + partyName + "!");
                    }
                }
            } else
            {
                Party party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer) player);
                PartyMember affectedMember = PartyController.instance().getMember(username);

                if (party != null)
                {
                    if ((party.isLeader(potentialLeader)) || (party.getType() == PartyType.打开) || ((potentialLeader != null) && (potentialLeader.username.toLowerCase().equalsIgnoreCase(affectedMember.username)) && (!adding)) || (party.isRequestedPlayer(username)))
                    {
                        if ((adding) && (affectedMember == null))
                        {
                            PartyController.instance().joinParty(party, new PartyMember(username, ""), false);
                        } else
                        {
                            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

                            PartyController.instance().leaveParty(party, affectedMember, false);

                            if ((dungeon != null) && (!dungeon.hasStarted()))
                            {
                                DungeonHandler.instance().checkForQueue(dungeon);

                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(dungeon));
                            }
                        }

                        sendPacketToAllExcept(AetherPacketHandler.sendPartyMemberChange(adding, partyName, username, skinURL), player);
                    } else
                    {
                        System.out.println("A player (" + potentialLeader.username + ") tried to add/kick a member (" + affectedMember.username + ") but didn't have permission or the party was not 'open'.");
                    }
                } else if (affectedMember != null)
                    System.out.println("Something went wrong! The player " + affectedMember.username + " tried to join/leave a null party!");
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketPartyMemberChange
 * JD-Core Version:    0.6.2
 */