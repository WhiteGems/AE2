package net.aetherteam.aether.party;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import java.io.PrintStream;
import java.util.ArrayList;

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

        if (side.isClient())
        {
            return clientController;
        }
        return serverController;
    }

    public boolean addParty(Party party, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        try
        {
            if (party != null)
            {
                if (getParty(party.getName()) == null)
                {
                    this.parties.add(party);

                    if ((sendPackets) && (side.isClient()))
                    {
                        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(true, party.getName(), party.getLeader().username, party.getLeader().skinUrl));
                    }

                    return true;
                }
            } else throw new PartyFunctionException("The party getting added was null! Aborting!");
        } catch (PartyFunctionException exception)
        {
            exception.printStackTrace();
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

            if (side.isClient())
            {
                if (party.hasMember(getMember(ClientNotificationHandler.clientUsername())))
                {
                    ClientNotificationHandler.createGeneric("公会已解散!", "", "");
                }
            }

            this.parties.remove(party);

            if ((sendPackets) && (side == Side.CLIENT))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(false, party.getName(), party.getLeader().username, party.getLeader().skinUrl));
            }

            return true;
        }
        System.out.println("A party was trying to remove itself, but unfortunately doesn't exist :(");

        return false;
    }

    public boolean promoteMember(PartyMember member, MemberType type, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((member != null) && (type != null))
        {
            Party party = getParty(member);

            if (party != null)
            {
                party.promoteMember(member, type);

                if ((sendPackets) && (side.isClient()))
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendMemberTypeChange(member.username, type));
                }

                return true;
            }
        } else
        {
            System.out.println("A party member was trying to promote itself, but unfortunately doesn't exist :(");
        }

        return false;
    }

    public void requestPlayer(Party party, PartyMember member, String requestedPlayer, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party != null) && (!requestedPlayer.isEmpty()) && (requestedPlayer != null))
        {
            party.queueRequestedPlayer(requestedPlayer);

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(true, party.getName(), member.username, member.skinUrl, requestedPlayer));
            }
        }
    }

    public void removePlayerRequest(Party party, PartyMember member, String requestedPlayer, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party != null) && (!requestedPlayer.isEmpty()) && (requestedPlayer != null))
        {
            party.removeRequestedPlayer(requestedPlayer);

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(false, party.getName(), member.username, member.skinUrl, requestedPlayer));
            }
        }
    }

    public void joinParty(Party party, PartyMember member, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party != null) && (member != null))
        {
            if (side.isClient())
            {
                if (party.hasMember(getMember(ClientNotificationHandler.clientUsername())))
                {
                    ClientNotificationHandler.createGeneric("新成员加入!", member.username, "");
                }
            }

            party.join(member);

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(true, party.getName(), member.username, member.skinUrl));
            }
        }
    }

    public void leaveParty(Party party, PartyMember member, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party.hasMember(member)) && (party != null) && (member != null))
        {
            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

            if (dungeon != null)
            {
                dungeon.disbandMember(member);
            }

            if (side.isClient())
            {
                if ((party.hasMember(getMember(ClientNotificationHandler.clientUsername()))) && (!member.username.equalsIgnoreCase(ClientNotificationHandler.clientUsername())))
                {
                    ClientNotificationHandler.createGeneric("成员离开!", member.username, "");
                }
            }

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), member.username, member.skinUrl));
            }

            party.leave(member);
        }
    }

    public boolean changePartyName(Party party, String newName, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party != null) && (!newName.isEmpty()) && (getParty(newName) == null))
        {
            if (side.isClient())
            {
                if (party.hasMember(getMember(ClientNotificationHandler.clientUsername())))
                {
                    ClientNotificationHandler.createGeneric("公会名称改变!", "新名称为: " + newName, "");
                }
            }

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyNameChange(party.getName(), newName));
            }

            party.setName(newName);

            return true;
        }

        return false;
    }

    public void changePartyType(Party party, PartyType newType, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.parties.contains(party)) && (party != null) && (newType != null))
        {
            if (side.isClient())
            {
                if (party.hasMember(getMember(ClientNotificationHandler.clientUsername())))
                {
                    ClientNotificationHandler.createGeneric("公会类型改变!", "现在是: " + newType.name(), "");
                }
            }

            party.setType(newType);

            if ((sendPackets) && (side == Side.CLIENT))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(party.getName(), newType));
            }
        }
    }

    public Party getParty(String partyName)
    {
        for (Party party : this.parties)
        {
            if (party.getName().equalsIgnoreCase(partyName))
            {
                return party;
            }
        }

        return null;
    }

    public Party getParty(EntityPlayer player)
    {
        PartyMember member = getMember(player);

        for (Party party : this.parties)
        {
            if (party.getMembers().contains(member))
            {
                return party;
            }
        }

        return null;
    }

    public Party getParty(PartyMember member)
    {
        for (Party party : this.parties)
        {
            if (party.getMembers().contains(member))
            {
                return party;
            }
        }

        return null;
    }

    public PartyMember getMember(String playerUsername)
    {
        for (Party party : this.parties)
        {
            for (PartyMember member : party.getMembers())
            {
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
        return getMember(player.username);
    }

    public boolean inParty(Party party, String playerUsername)
    {
        return (getMember(playerUsername) != null) && (party.hasMember(getMember(playerUsername)));
    }

    public boolean inParty(Party party, EntityPlayer player)
    {
        return inParty(party, player.username);
    }

    public boolean inParty(Party party, PartyMember member)
    {
        return inParty(party, member.username);
    }

    public boolean inParty(String playerUsername)
    {
        return getMember(playerUsername) != null;
    }

    public boolean inParty(EntityPlayer player)
    {
        return inParty(player.username);
    }

    public boolean inParty(PartyMember member)
    {
        return inParty(member.username);
    }

    public boolean isLeader(String playerUsername)
    {
        return (getMember(playerUsername) != null) && (getMember(playerUsername).isLeader());
    }

    public boolean isLeader(EntityPlayer player)
    {
        return isLeader(player.username);
    }

    public boolean isLeader(PartyMember member)
    {
        return isLeader(member.username);
    }

    public void setParties(ArrayList parties)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        this.parties = parties;
    }

    public ArrayList getParties()
    {
        return this.parties;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.party.PartyController
 * JD-Core Version:    0.6.2
 */