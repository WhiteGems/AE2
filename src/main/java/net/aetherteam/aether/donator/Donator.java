package net.aetherteam.aether.donator;

import java.util.HashMap;

public class Donator
{
    public String username;
    String RSA;
    public HashMap<EnumChoiceType, DonatorChoice> choices = new HashMap();

    public Donator(String username, String RSA)
    {
        this.username = username;
        this.RSA = RSA;
    }

    public String getRSA()
    {
        return this.RSA;
    }

    public void addChoice(DonatorChoice choice)
    {
        if (this.choices.get(choice.type) != null)
        {
            this.choices.remove(choice.type);
        }

        this.choices.put(choice.type, choice);
    }

    public void removeChoiceType(EnumChoiceType type)
    {
        if (this.choices.get(type) != null)
        {
            this.choices.remove(type);
        }
    }

    public boolean containsChoiceType(EnumChoiceType type)
    {
        return this.choices.get(type) != null;
    }

    public DonatorChoice getChoiceFromType(EnumChoiceType type)
    {
        return this.choices.get(type) != null ? (DonatorChoice)this.choices.get(type) : null;
    }
}
