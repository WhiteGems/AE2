package net.aetherteam.aether.dungeons.keys;

import net.aetherteam.aether.entities.bosses.EntityKey;

import java.io.Serializable;

public enum EnumKeyType implements Serializable
{
    Host("主宰", "Host"),
    Guardian("守卫者", "Guardian"),
    Eye("迷宫之眼", "Eye");
    private String texture;
    private String keyName;

    private EnumKeyType(String keyName, String textureName)
    {
        this.texture = ("/net/aetherteam/aether/client/sprites/gui/key/" + textureName + ".png");
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

        for (EnumKeyType var4 : var1)
        {
            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
