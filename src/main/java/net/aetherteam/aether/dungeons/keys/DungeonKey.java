package net.aetherteam.aether.dungeons.keys;

import java.io.Serializable;

public class DungeonKey implements Serializable
{
    EnumKeyType type;

    public DungeonKey(EnumKeyType var1)
    {
        this.type = var1;
    }

    public EnumKeyType getType()
    {
        return this.type;
    }
}