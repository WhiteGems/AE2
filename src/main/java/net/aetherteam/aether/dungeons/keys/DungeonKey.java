package net.aetherteam.aether.dungeons.keys;

import java.io.Serializable;

public class DungeonKey implements Serializable
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