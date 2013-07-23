package net.aetherteam.aether.party;

import java.io.Serializable;

public enum PartyType implements Serializable
{
    OPEN(6750054),
    CLOSED(16711680),
    PRIVATE(0);
    public int displayColor;

    private PartyType(int var3)
    {
        this.displayColor = var3;
    }

    public int getDisplayColor()
    {
        return this.displayColor;
    }

    public static PartyType getTypeFromString(String var0)
    {
        PartyType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            PartyType var4 = var1[var3];

            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
