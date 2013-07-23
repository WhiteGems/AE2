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

    private MemberType(boolean var3, boolean var4, boolean var5, boolean var6)
    {
        this.canKick = var3;
        this.canBan = var4;
        this.canRecruit = var5;
        this.canPromote = var6;
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

    public static MemberType getTypeFromString(String var0)
    {
        MemberType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            MemberType var4 = var1[var3];

            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
