package net.aetherteam.aether.donator;

public enum EnumChoiceType
{
    MOA("MOA", "/net/aetherteam/aether/client/sprites/moa/", "http://www.aethermod.net/skins/moa_"),
    CAPE("CAPE", "/net/aetherteam/aether/client/sprites/capes/", "http://www.aethermod.net/skins/cape_");

    public String name;
    public String localTexturePath;
    public String onlineTexturePath;

    private EnumChoiceType(String name, String texturePath, String onlinePath)
    {
        this.name = name;
        this.localTexturePath = texturePath;
        this.onlineTexturePath = onlinePath;
    }

    public static EnumChoiceType getTypeFromString(String name)
    {
        for (EnumChoiceType v : values())
        {
            if (v.name().equals(name))
            {
                return v;
            }
        }

        return null;
    }
}

