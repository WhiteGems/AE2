package net.aetherteam.aether.party;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.exceptions.PartyFunctionException;
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;

public class PartyController
{
    private ArrayList<Party> parties = new ArrayList();
    private static PartyController clientController = new PartyController();
    private static PartyController serverController = new PartyController();

    public static PartyController instance()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        return side.isClient() ? clientController : serverController;
    }

    public boolean addParty(Party party, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        try
        {
            if (party == null)
            {
                throw new PartyFunctionException("The party getting added was null! Aborting!");
            }

            if (this.getParty(party.getName()) == null)
            {
                this.parties.add(party);

                if (sendPackets && side.isClient())
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(true, party.getName(), party.getLeader().username));
                }

                return true;
            }
        }
        catch (PartyFunctionException var5)
        {
            var5.printStackTrace();
        }

        return false;
    }

    public boolean removeParty(Party party, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (party != null)
        {
            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

            if (dungeon != null)
            {
                dungeon.disbandQueue(party);
            }

            if (side.isClient() && party.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("公会已解散!", "", "");
            }

            this.parties.remove(party);

            if (sendPackets && side == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(false, party.getName(), party.getLeader().username));
            }

            return true;
        }
        else
        {
            System.out.println("A party was trying to remove itself, but unfortunately doesn\'t exist :(");
            return false;
        }
    }

    public boolean promoteMember(PartyMember member, MemberType type, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (member != null && type != null)
        {
            Party party = this.getParty(member);

            if (party != null)
            {
                party.promoteMember(member, type);

                if (sendPackets && side.isClient())
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendMemberTypeChange(member.username, type));
                }

                return true;
            }
        }
        else
        {
            System.out.println("A party member was trying to promote itself, but unfortunately doesn\'t exist :(");
        }

        return false;
    }

    public void requestPlayer(Party party, PartyMember member, String requestedPlayer, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party != null && !requestedPlayer.isEmpty() && requestedPlayer != null)
        {
            party.queueRequestedPlayer(requestedPlayer);

            if (sendPackets && side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(true, party.getName(), member.username, requestedPlayer));
            }
        }
    }

    public void removePlayerRequest(Party party, PartyMember member, String requestedPlayer, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party != null && !requestedPlayer.isEmpty() && requestedPlayer != null)
        {
            party.removeRequestedPlayer(requestedPlayer);

            if (sendPackets && side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(false, party.getName(), member.username, requestedPlayer));
            }
        }
    }

    public void joinParty(Party party, PartyMember member, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party != null && member != null)
        {
            if (side.isClient() && party.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("新成员加入!", member.username, "");
            }

            party.join(member);

            if (sendPackets && side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(true, party.getName(), member.username));
            }
        }
    }

    public void leaveParty(Party party, PartyMember member, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party.hasMember(member) && party != null && member != null)
        {
            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

            if (dungeon != null)
            {
                dungeon.disbandMember(member);
            }

            if (side.isClient() && party.hasMember(this.getMember(ClientNotificationHandler.clientUsername())) && !member.username.equalsIgnoreCase(ClientNotificationHandler.clientUsername()))
            {
                ClientNotificationHandler.createGeneric("成员离开!", member.username, "");
            }

            if (sendPackets && side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), member.username));
            }

            party.leave(member);
        }
    }

    public boolean changePartyName(Party party, String newName, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party != null && !newName.isEmpty() && this.getParty(newName) == null)
        {
            if (side.isClient() && party.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("公会名称改变!", "新名称为: " + newName, "");
            }

            if (sendPackets && side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyNameChange(party.getName(), newName));
            }

            party.setName(newName);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void changePartyType(Party party, PartyType newType, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(party) && party != null && newType != null)
        {
            if (side.isClient() && party.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("公会类型改变!", "现在是: " + newType.name(), "");
            }

            party.setType(newType);

            if (sendPackets && side == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(party.getName(), newType));
            }
        }
    }

    public Party getParty(String partyName)
    {
        Iterator i$ = this.parties.iterator();
        Party party;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            party = (Party)i$.next();
        }
        while (!party.getName().equalsIgnoreCase(partyName));

        return party;
    }

    public Party getParty(EntityPlayer player)
    {
        PartyMember member = this.getMember(player);
        Iterator i$ = this.parties.iterator();
        Party party;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            party = (Party)i$.next();
        }
        while (!party.getMembers().contains(member));

        return party;
    }

    public Party getParty(PartyMember member)
    {
        Iterator i$ = this.parties.iterator();
        Party party;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            party = (Party)i$.next();
        }
        while (!party.getMembers().contains(member));

        return party;
    }

    public PartyMember getMember(String playerUsername)
    {
        Iterator i$ = this.parties.iterator();

        while (i$.hasNext())
        {
            Party party = (Party)i$.next();
            Iterator i$1 = party.getMembers().iterator();

            while (i$1.hasNext())
            {
                PartyMember member = (PartyMember)i$1.next();

                if (member.username.equalsIgnoreCase(playerUsername))
                {
                    return member;
                }
            }
        }

        return null;
    }

    public PartyMember getMember(EntityPlayer player)
    {
        return this.getMember(player.username);
    }

    public boolean inParty(Party party, String playerUsername)
    {
        return this.getMember(playerUsername) != null && party.hasMember(this.getMember(playerUsername));
    }

    public boolean inParty(Party party, EntityPlayer player)
    {
        return this.inParty(party, player.username);
    }

    public boolean inParty(Party party, PartyMember member)
    {
        return this.inParty(party, member.username);
    }

    public boolean inParty(String playerUsername)
    {
        return this.getMember(playerUsername) != null;
    }

    public boolean inParty(EntityPlayer player)
    {
        return this.inParty(player.username);
    }

    public boolean inParty(PartyMember member)
    {
        return this.inParty(member.username);
    }

    public boolean isLeader(String playerUsername)
    {
        return this.getMember(playerUsername) != null && this.getMember(playerUsername).isLeader();
    }

    public boolean isLeader(EntityPlayer player)
    {
        return this.isLeader(player.username);
    }

    public boolean isLeader(PartyMember member)
    {
        return this.isLeader(member.username);
    }

    public void setParties(ArrayList<Party> parties)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        this.parties = parties;
    }

    public ArrayList<Party> getParties()
    {
        return this.parties;
    }
}
