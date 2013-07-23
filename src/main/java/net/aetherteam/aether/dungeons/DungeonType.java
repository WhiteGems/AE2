package net.aetherteam.aether.dungeons;

public enum DungeonType
{
    BRONZE("滑行者迷宫", 6),
    SILVER("女武神殿堂", 6),
    GOLD("太阳神堡垒", 6);

    String dungeonName;
    int keepCap;

    private DungeonType(String var3, int var4)
    {
        this.dungeonName = var3;
        this.keepCap = var4;
    }

    public String getName()
    {
        return this.dungeonName;
    }

    public int getKeepCap()
    {
        return this.keepCap;
    }

    public static DungeonType getTypeFromString(String var0)
    {
        DungeonType[] var1 = values();

        for (DungeonType var4 : var1)
        {
            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
