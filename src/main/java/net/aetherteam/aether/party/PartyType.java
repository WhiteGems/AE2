package net.aetherteam.aether.party;

import java.io.Serializable;

public enum PartyType implements Serializable
{
    OPEN(6750054),
    CLOSED(16711680),
    PRIVATE(0);
    public int displayColor;

    private PartyType(int displayColor)
    {
        this.displayColor = displayColor;
    }

    public int getDisplayColor()
    {
        return this.displayColor;
    }

    public static PartyType getTypeFromString(String name)
    {
        PartyType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            PartyType type = arr$[i$];

            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}
