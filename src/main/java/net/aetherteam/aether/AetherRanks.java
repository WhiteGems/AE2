package net.aetherteam.aether;

import java.util.ArrayList;

public enum AetherRanks
{
    DEVELOPER("Aether II Developer", Aether.developers, 2342342),
    HELPER("Aether II Helper", Aether.helper, 2344),
    DEFAULT("", new ArrayList(), 0);
    private ArrayList members = new ArrayList();
    private int descriptionColor;
    private String description;

    public static void addAllRanks()
    {
        Aether.developers.add("ijaryt23");
        Aether.developers.add("hemile");
        Aether.developers.add("dark3304");
        Aether.developers.add("kingbdogz");
        Aether.developers.add("mike4560");
        Aether.developers.add("libertyprimetf2");
        Aether.developers.add("ozzar0th");
        Aether.helper.add("ijevin");
        Aether.helper.add("mr360games");
        Aether.helper.add("captainsparklez");
        Aether.helper.add("clashjtm");
        Aether.helper.add("chimneyswift");
        Aether.helper.add("craftnode");
        Aether.helper.add("wyld");
        Aether.helper.add("themattabase");
    }

    private AetherRanks(String var3, ArrayList var4, int var5)
    {
        this.members = var4;
        this.description = var3;
        this.descriptionColor = var5;
    }

    public int getDescriptionColor()
    {
        return this.descriptionColor;
    }

    public String getDescription()
    {
        return this.description;
    }

    public static AetherRanks getRankFromMember(String var0)
    {
        for (int var1 = 0; var1 < values().length; ++var1)
        {
            if (values()[var1].getMembers().contains(var0.toLowerCase()))
            {
                return values()[var1];
            }
        }

        return DEFAULT;
    }

    public ArrayList getMembers()
    {
        return this.members;
    }
}
