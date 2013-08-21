package net.aetherteam.aether.donator;

public enum EnumChoiceType
{
    MOA("MOA", "aether:textures/moa/", "http://www.aethermod.net/skins/moa_"),
    CAPE("CAPE", "aether:textures/capes/", "http://www.aethermod.net/skins/cape_");
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
        EnumChoiceType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            EnumChoiceType v = arr$[i$];

            if (v.name().equals(name))
            {
                return v;
            }
        }

        return null;
    }
}
