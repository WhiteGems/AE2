package net.aetherteam.aether.dungeons;

public enum DungeonType
{
    BRONZE("滑行者迷宫", 6), SILVER("女武神殿堂", 6), GOLD("太阳神堡垒", 6);

    String dungeonName;
    int keepCap;

    private DungeonType(String dungeonName, int keepCap)
    {
        this.dungeonName = dungeonName;
        this.keepCap = keepCap;
    }

    public String getName()
    {
        return this.dungeonName;
    }

    public int getKeepCap()
    {
        return this.keepCap;
    }

    public static DungeonType getTypeFromString(String name)
    {
        for (DungeonType type : values())
        {
            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.dungeons.DungeonType
 * JD-Core Version:    0.6.2
 */