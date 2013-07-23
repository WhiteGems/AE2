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

