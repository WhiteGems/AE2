package net.aetherteam.playercore_api.cores;

import net.minecraft.entity.Entity;

public interface IPlayerCoreCommon
{
    boolean isJumping();

    void faceEntity(Entity var1, float var2, float var3);
}
