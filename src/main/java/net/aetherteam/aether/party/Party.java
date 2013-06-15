package net.aetherteam.aether.party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;

public class Party implements Serializable
{
    private String name;
    private PartyMember leader;
    private ArrayList members = new ArrayList();
    private ArrayList requestedMembers = new ArrayList();
    private int memberSizeLimit = 20;
    private PartyType TYPE;

    public Party(String name, PartyMember leader)
    {
        this.TYPE = PartyType.OPEN;
        this.name = name;
        this.leader = leader;
        this.leader.promoteTo(MemberType.LEADER);
        this.join(leader);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String text)
    {
        this.name = text;
    }

    public int getMemberSizeLimit()
    {
        return this.memberSizeLimit;
    }

    public void setMemberSizeLimit(int size)
    {
        this.memberSizeLimit = size;
    }

    public PartyType getType()
    {
        return this.TYPE;
    }

    public Party setType(PartyType type)
    {
        this.TYPE = type;
        return this;
    }

    public int getSize()
    {
        return this.members.size();
    }

    public PartyMember getLeader()
    {
        return this.leader;
    }

    public boolean hasLeader()
    {
        return this.leader != null;
    }

    public boolean isLeader(PartyMember member)
    {
        return member != null && this.leader == member;
    }

    public boolean hasMember(PartyMember member)
    {
        return member != null && this.hasMember(member.username);
    }

    public boolean hasMember(String username)
    {
        if (this.members != null && username != null)
        {

            for (Object member : this.members)
            {
                PartyMember var3 = (PartyMember) member;

                if (var3.username.equalsIgnoreCase(username))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void promoteMember(PartyMember member, MemberType type)
    {
        if (this.hasMember(member))
        {
            member.promoteTo(type);

            if (type == MemberType.LEADER && this.hasLeader())
            {
                this.leader.promoteTo(MemberType.MEMBER);
                this.leader = member;
            }
        }
    }

    public ArrayList<PartyMember> getMembers()
    {
        return this.members;
    }

    public void queueRequestedPlayer(String playerUsername)
    {
        this.requestedMembers.add(playerUsername.toLowerCase());
    }

    public void removeRequestedPlayer(String playerUsername)
    {
        this.requestedMembers.remove(playerUsername.toLowerCase());
    }

    public boolean isRequestedPlayer(String playerUsername)
    {
        return this.requestedMembers.contains(playerUsername.toLowerCase());
    }

    public void join(PartyMember member)
    {
        if (member != null && this.members.size() < this.memberSizeLimit && !this.hasMember(member))
        {
            if (PartyController.instance().inParty(member.username))
            {
                PartyController.instance().getParty(PartyController.instance().getMember(member.username));
            }

            if (this.isRequestedPlayer(member.username))
            {
                this.removeRequestedPlayer(member.username);
            }

            this.members.add(member);
        }
    }

    public void leave(PartyMember member)
    {
        if (this.hasMember(member))
        {
            if (member.isLeader() && this.members.size() > 1)
            {
                Random rand = new Random();
                PartyMember newLeader;

                for (newLeader = null; newLeader == null || newLeader == member; newLeader = (PartyMember)this.members.get(rand.nextInt(this.members.size())))
                {
                    ;
                }

                if (newLeader != null)
                {
                    PartyController.instance().promoteMember(PartyController.instance().getMember(newLeader.username), MemberType.LEADER, true);
                }
            }

            this.members.remove(member);

            if (this.members.size() <= 0)
            {
                PartyController.instance().removeParty(this, true);
            }
        }
    }
}
