package net.aetherteam.aether.dungeons.keys;

import net.aetherteam.aether.entities.bosses.EntityKey;

public enum EnumKeyType
{
    Host("Host"),
    Guardian("Guardian"),
    Eye("Eye");
    private String texture;
    private String keyName;

    private EnumKeyType(String keyName)
    {
        this.texture = "/net/aetherteam/aether/client/sprites/gui/key/" + keyName + ".png";
        this.keyName = keyName;
    }

    public String getTexture()
    {
        return this.texture;
    }

    public String getKeyName()
    {
        return this.keyName;
    }

    public static EnumKeyType getEnumFromItem(EntityKey key)
    {
        for (int i = 0; i < values().length; ++i)
        {
            if (key.getKeyName().contains(values()[i].keyName))
            {
                return values()[i];
            }
        }

        return null;
    }

    public static EnumKeyType getTypeFromString(String name)
    {
        EnumKeyType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            EnumKeyType type = arr$[i$];

            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}
