package net.aetherteam.aether.donator;

public enum EnumChoiceType
{
    MOA("MOA", "/net/aetherteam/aether/client/sprites/moa/", "http://www.aethermod.net/skins/moa_"),
    CAPE("CAPE", "/net/aetherteam/aether/client/sprites/capes/", "http://www.aethermod.net/skins/cape_");
    public String name;
    public String localTexturePath;
    public String onlineTexturePath;

    private EnumChoiceType(String var3, String var4, String var5)
    {
        this.name = var3;
        this.localTexturePath = var4;
        this.onlineTexturePath = var5;
    }

    public static EnumChoiceType getTypeFromString(String var0)
    {
        EnumChoiceType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            EnumChoiceType var4 = var1[var3];

            if (var4.name().equals(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
