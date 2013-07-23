package net.aetherteam.aether.interfaces;

import net.aetherteam.aether.enums.EnumBossType;
import net.minecraft.entity.Entity;

public interface IAetherBoss
{
    int getBossHP();

    int getBossMaxHP();

    int getBossEntityID();

    String getBossTitle();

    Entity getBossEntity();

    int getBossStage();

    EnumBossType getBossType();
}
