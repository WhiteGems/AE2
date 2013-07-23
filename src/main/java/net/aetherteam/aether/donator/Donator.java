package net.aetherteam.aether.donator;

import java.util.HashMap;

public class Donator
{
    public String username;
    String RSA;
    public HashMap choices = new HashMap();

    public Donator(String var1, String var2)
    {
        this.username = var1;
        this.RSA = var2;
    }

    public String getRSA()
    {
        return this.RSA;
    }

    public void addChoice(DonatorChoice var1)
    {
        if (this.choices.get(var1.type) != null)
        {
            this.choices.remove(var1.type);
        }

        this.choices.put(var1.type, var1);
    }

    public void removeChoiceType(EnumChoiceType var1)
    {
        if (this.choices.get(var1) != null)
        {
            this.choices.remove(var1);
        }
    }

    public boolean containsChoiceType(EnumChoiceType var1)
    {
        return this.choices.get(var1) != null;
    }

    public DonatorChoice getChoiceFromType(EnumChoiceType var1)
    {
        return this.choices.get(var1) != null ? (DonatorChoice)this.choices.get(var1) : null;
    }
}
