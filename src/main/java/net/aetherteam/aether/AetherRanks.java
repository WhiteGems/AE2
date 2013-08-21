package net.aetherteam.aether;

import java.util.ArrayList;

public enum AetherRanks
{
    DEVELOPER("以太Ⅱ 开发者", Aether.developers, 2342342),
    HELPER("以太Ⅱ 帮助者", Aether.helper, 2344),
    TRANSLATOR("以太Ⅱ 汉化人员", Aether.translator, 2342342),
    DEFAULT("", new ArrayList(), 0);
    private ArrayList<String> members = new ArrayList();
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
        Aether.translator.add("crafteverywhere");
        Aether.translator.add("pa001024");
        Aether.translator.add("sun");
        Aether.translator.add("waidely");
        Aether.translator.add("wjmz8mr");
        Aether.translator.add("zestybaby");
    }

    private AetherRanks(String description, ArrayList members, int descriptionColor)
    {
        this.members = members;
        this.description = description;
        this.descriptionColor = descriptionColor;
    }

    public int getDescriptionColor()
    {
        return this.descriptionColor;
    }

    public String getDescription()
    {
        return this.description;
    }

    public static AetherRanks getRankFromMember(String member)
    {
        for (int i = 0; i < values().length; ++i)
        {
            if (values()[i].getMembers().contains(member.toLowerCase()))
            {
                return values()[i];
            }
        }

        return DEFAULT;
    }

    public ArrayList<String> getMembers()
    {
        return this.members;
    }
}
