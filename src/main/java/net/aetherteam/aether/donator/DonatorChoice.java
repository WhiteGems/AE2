package net.aetherteam.aether.donator;

import java.util.ArrayList;

public class DonatorChoice
{
    public String name;
    public EnumChoiceType type;
    public DonatorTexture textureFile;
    public static ArrayList choices = new ArrayList();

    public DonatorChoice(String name, EnumChoiceType type, DonatorTexture texture)
    {
        this.name = name;
        this.type = type;

        texture.localURL = (type.localTexturePath + texture.localURL);
        texture.onlineURL = (type.onlineTexturePath + texture.onlineURL);

        this.textureFile = texture;

        choices.add(this);
    }

    public static DonatorChoice getChoiceFromString(String name)
    {
        for (int i = 0; i < choices.size(); i++)
        {
            if (((DonatorChoice) choices.get(i)).name.equalsIgnoreCase(name))
            {
                return (DonatorChoice) choices.get(i);
            }
        }

        return null;
    }

    public boolean isType(EnumChoiceType type)
    {
        return this.type == type;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.donator.DonatorChoice
 * JD-Core Version:    0.6.2
 */