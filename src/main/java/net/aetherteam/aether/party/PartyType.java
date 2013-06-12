package net.aetherteam.aether.party;

import java.io.Serializable;

public enum PartyType implements Serializable
{
    OPEN(6750054, "公开"), CLOSE(16711680, "关闭"), PRIVATE(0, "私有");

    public int displayColor;
    public transient String name;

    private PartyType(int displayColor, String name)
    {
        this.displayColor = displayColor;
        this.name = name;
    }

    public int getDisplayColor()
    {
        return this.displayColor;
    }

    public static PartyType getTypeFromString(String name)
    {
        for (PartyType type : values())
        {
            if (type.name.equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}
