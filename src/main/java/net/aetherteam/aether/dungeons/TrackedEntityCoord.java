package net.aetherteam.aether.dungeons;

import java.io.Serializable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class TrackedEntityCoord implements Serializable
{
    private String entityName;
    private float x;
    private float y;
    private float z;

    public TrackedEntityCoord(float var1, float var2, float var3, String var4)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
        this.entityName = var4;
    }

    public EntityLiving getTrackedEntity(World var1)
    {
        return (EntityLiving)EntityList.createEntityByName(this.entityName, var1);
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public float getZ()
    {
        return this.z;
    }
}
