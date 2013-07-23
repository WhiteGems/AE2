package net.aetherteam.aether.dungeons;

public enum DungeonType
{
    BRONZE("Slider\'s Labyrinth", 6),
    SILVER("Valkyrie Temple", 6),
    GOLD("Fortress of the Sun Spirit", 6);
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
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            DungeonType var4 = var1[var3];

            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
