package net.aetherteam.aether.dungeons.keys;

import net.aetherteam.aether.entities.bosses.EntityKey;

public enum EnumKeyType
{
    Host("Host"),
    Guardian("Guardian"),
    Eye("Eye");
    private String texture;
    private String keyName;

    private EnumKeyType(String var3)
    {
        this.texture = "/net/aetherteam/aether/client/sprites/gui/key/" + var3 + ".png";
        this.keyName = var3;
    }

    public String getTexture()
    {
        return this.texture;
    }

    public String getKeyName()
    {
        return this.keyName;
    }

    public static EnumKeyType getEnumFromItem(EntityKey var0)
    {
        for (int var1 = 0; var1 < values().length; ++var1)
        {
            if (var0.getKeyName().contains(values()[var1].keyName))
            {
                return values()[var1];
            }
        }

        return null;
    }

    public static EnumKeyType getTypeFromString(String var0)
    {
        EnumKeyType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            EnumKeyType var4 = var1[var3];

            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
