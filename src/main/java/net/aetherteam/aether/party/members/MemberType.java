package net.aetherteam.aether.party.members;

import java.io.Serializable;

public enum MemberType implements Serializable
{
    LEADER(true, true, true, true),
    MODERATOR(true, true, false, false),
    MEMBER(false, false, false, false);
    private boolean canKick;
    private boolean canBan;
    private boolean canRecruit;
    private boolean canPromote;

    private MemberType(boolean canKick, boolean canBan, boolean canRecruit, boolean canPromote)
    {
        this.canKick = canKick;
        this.canBan = canBan;
        this.canRecruit = canRecruit;
        this.canPromote = canPromote;
    }

    public boolean canKick()
    {
        return this.canKick;
    }

    public boolean canBan()
    {
        return this.canBan;
    }

    public boolean canRecruit()
    {
        return this.canRecruit;
    }

    public boolean canPromote()
    {
        return this.canPromote;
    }

    public static MemberType getTypeFromString(String name)
    {
        MemberType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            MemberType type = arr$[i$];

            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}
