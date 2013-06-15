package net.aetherteam.aether;

import java.util.ArrayList;

public enum AetherRanks
{
    DEVELOPER("Aether II Developer", Aether.developers, 2342342), HELPER("Aether II Helper", Aether.helper, 2344), TRANSLATOR("以太Ⅱ 汉化人员", Aether.translator, 2342342), DEFAULT("", new ArrayList(), 0);

    private ArrayList members = new ArrayList();

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
        Aether.helper.add("clashjtm");
        Aether.helper.add("chimneyswift");
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
    }

    public static AetherRanks getRankFromMember(String member)
    {
        for (int i = 0; i < values().length; i++)
        {
            if (values()[i].getMembers().contains(member.toLowerCase()))
            {
                return values()[i];
            }
        }

        return DEFAULT;
    }

    public ArrayList getMembers()
    {
        return this.members;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherRanks
 * JD-Core Version:    0.6.2
 */