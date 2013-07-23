package net.aetherteam.aether.dungeons;

public enum DungeonType
{
    BRONZE("Slider's Labyrinth", 6), SILVER("Valkyrie Temple", 6), GOLD("Fortress of the Sun Spirit", 6);

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

