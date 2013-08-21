package net.aetherteam.aether.dungeons.keys;

public class DungeonKey
{
    EnumKeyType type;

    public DungeonKey(EnumKeyType type)
    {
        this.type = type;
    }

    public EnumKeyType getType()
    {
        return this.type;
    }
}
