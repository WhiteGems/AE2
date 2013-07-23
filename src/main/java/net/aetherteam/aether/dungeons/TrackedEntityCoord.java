package net.aetherteam.aether.dungeons;

import java.io.Serializable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class TrackedEntityCoord
    implements Serializable
{
    private String entityName;
    private float x;
    private float y;
    private float z;

    public TrackedEntityCoord(float x, float y, float z, String entityName)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityName = entityName;
    }

    public EntityLiving getTrackedEntity(World world)
    {
        return (EntityLiving)EntityList.createEntityByName(this.entityName, world);
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

