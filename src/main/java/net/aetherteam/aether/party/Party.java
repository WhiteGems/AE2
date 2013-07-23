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

    public Party(String var1, PartyMember var2)
    {
        this.TYPE = PartyType.OPEN;
        this.name = var1;
        this.leader = var2;
        this.leader.promoteTo(MemberType.LEADER);
        this.join(var2);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String var1)
    {
        this.name = var1;
    }

    public int getMemberSizeLimit()
    {
        return this.memberSizeLimit;
    }

    public void setMemberSizeLimit(int var1)
    {
        this.memberSizeLimit = var1;
    }

    public PartyType getType()
    {
        return this.TYPE;
    }

    public Party setType(PartyType var1)
    {
        this.TYPE = var1;
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

    public boolean isLeader(PartyMember var1)
    {
        return var1 != null && this.leader == var1;
    }

    public boolean hasMember(PartyMember var1)
    {
        return var1 != null && this.hasMember(var1.username);
    }

    public boolean hasMember(String var1)
    {
        if (this.members != null && var1 != null)
        {
            Iterator var2 = this.members.iterator();

            while (var2.hasNext())
            {
                PartyMember var3 = (PartyMember)var2.next();

                if (var3.username.equalsIgnoreCase(var1))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void promoteMember(PartyMember var1, MemberType var2)
    {
        if (this.hasMember(var1))
        {
            var1.promoteTo(var2);

            if (var2 == MemberType.LEADER && this.hasLeader())
            {
                this.leader.promoteTo(MemberType.MEMBER);
                this.leader = var1;
            }
        }
    }

    public ArrayList getMembers()
    {
        return this.members;
    }

    public void queueRequestedPlayer(String var1)
    {
        this.requestedMembers.add(var1.toLowerCase());
    }

    public void removeRequestedPlayer(String var1)
    {
        this.requestedMembers.remove(var1.toLowerCase());
    }

    public boolean isRequestedPlayer(String var1)
    {
        return this.requestedMembers.contains(var1.toLowerCase());
    }

    public void join(PartyMember var1)
    {
        if (var1 != null && this.members.size() < this.memberSizeLimit && !this.hasMember(var1))
        {
            if (PartyController.instance().inParty(var1.username))
            {
                PartyController.instance().getParty(PartyController.instance().getMember(var1.username));
            }

            if (this.isRequestedPlayer(var1.username))
            {
                this.removeRequestedPlayer(var1.username);
            }

            this.members.add(var1);
        }
    }

    public void leave(PartyMember var1)
    {
        if (this.hasMember(var1))
        {
            if (var1.isLeader() && this.members.size() > 1)
            {
                Random var2 = new Random();
                PartyMember var3;

                for (var3 = null; var3 == null || var3 == var1; var3 = (PartyMember)this.members.get(var2.nextInt(this.members.size())))
                {
                    ;
                }

                if (var3 != null)
                {
                    PartyController.instance().promoteMember(PartyController.instance().getMember(var3.username), MemberType.LEADER, true);
                }
            }

            this.members.remove(var1);

            if (this.members.size() <= 0)
            {
                PartyController.instance().removeParty(this, true);
            }
        }
    }
}
