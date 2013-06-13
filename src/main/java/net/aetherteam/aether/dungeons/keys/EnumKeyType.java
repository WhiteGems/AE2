package net.aetherteam.aether.dungeons.keys;

import net.aetherteam.aether.entities.bosses.EntityKey;

public enum EnumKeyType
{
    Host("主宰", "Host"), Guardian("守卫者", "Guardian"), Eye("之眼", "Eye");

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

    public static EnumKeyType getEnumFromItem(EntityKey key)
    {
        for (int i = 0; i < values().length; i++)
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
        for (EnumKeyType type : values())
        {
            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.dungeons.keys.EnumKeyType
 * JD-Core Version:    0.6.2
 */