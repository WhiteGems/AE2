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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            boolean adding = dat.readBoolean();
            String partyName = dat.readUTF();
            String username = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            Party party;
            PartyMember potentialLeader;

            if (side.isClient())
            {
                party = PartyController.instance().getParty(partyName);

                if (party != null)
                {
                    if (adding)
                    {
                        potentialLeader = new PartyMember(username);
                        PartyController.instance().joinParty(party, potentialLeader, false);
                        System.out.println("Added Player \'" + username + "\' to the Party: " + partyName + "!");
                    }
                    else
                    {
                        potentialLeader = PartyController.instance().getMember(username);
                        PartyController.instance().leaveParty(party, potentialLeader, false);
                        System.out.println("Removed Player \'" + username + "\' from the Party: " + partyName + "!");
                    }
                }
            }
            else
            {
                party = PartyController.instance().getParty(partyName);
                potentialLeader = PartyController.instance().getMember((EntityPlayer)player);
                PartyMember affectedMember = PartyController.instance().getMember(username);

                if (party != null)
                {
                    if (!party.isLeader(potentialLeader) && party.getType() != PartyType.OPEN && (potentialLeader == null || !potentialLeader.username.toLowerCase().equalsIgnoreCase(affectedMember.username) || adding) && !party.isRequestedPlayer(username))
                    {
                        System.out.println("A player (" + potentialLeader.username + ") tried to add/kick a member (" + affectedMember.username + ") but didn\'t have permission or the party was not \'open\'.");
                    }
                    else
                    {
                        if (adding && affectedMember == null)
                        {
                            PartyController.instance().joinParty(party, new PartyMember(username), false);
                        }
                        else
                        {
                            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
                            PartyController.instance().leaveParty(party, affectedMember, false);

                            if (dungeon != null && !dungeon.hasStarted())
                            {
                                DungeonHandler.instance().checkForQueue(dungeon);
                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(dungeon));
                            }
                        }

                        this.sendPacketToAllExcept(AetherPacketHandler.sendPartyMemberChange(adding, partyName, username), player);
                    }
                }
                else if (affectedMember != null)
                {
                    System.out.println("Something went wrong! The player " + affectedMember.username + " tried to join/leave a null party!");
                }
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }
}
