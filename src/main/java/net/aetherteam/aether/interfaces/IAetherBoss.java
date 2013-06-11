package net.aetherteam.aether.interfaces;

import net.aetherteam.aether.enums.EnumBossType;
import net.minecraft.entity.Entity;

public abstract interface IAetherBoss
{
    public abstract int getBossHP();

    public abstract int getBossMaxHP();

    public abstract int getBossEntityID();

    public abstract String getBossTitle();

    public abstract Entity getBossEntity();

    public abstract int getBossStage();

    public abstract EnumBossType getBossType();
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.interfaces.IAetherBoss
 * JD-Core Version:    0.6.2
 */