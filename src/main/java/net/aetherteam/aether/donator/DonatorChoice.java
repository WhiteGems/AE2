package net.aetherteam.aether.donator;

import java.util.ArrayList;

public class DonatorChoice
{
    public String name;
    public EnumChoiceType type;
    public DonatorTexture textureFile;
    public static ArrayList choices = new ArrayList();

    public DonatorChoice(String var1, EnumChoiceType var2, DonatorTexture var3)
    {
        this.name = var1;
        this.type = var2;
        var3.localURL = var2.localTexturePath + var3.localURL;
        var3.onlineURL = var2.onlineTexturePath + var3.onlineURL;
        this.textureFile = var3;
        choices.add(this);
    }

    public static DonatorChoice getChoiceFromString(String var0)
    {
        for (int var1 = 0; var1 < choices.size(); ++var1)
        {
            if (((DonatorChoice)choices.get(var1)).name.equalsIgnoreCase(var0))
            {
                return (DonatorChoice)choices.get(var1);
            }
        }

        return null;
    }

    public boolean isType(EnumChoiceType var1)
    {
        return this.type == var1;
    }
}
