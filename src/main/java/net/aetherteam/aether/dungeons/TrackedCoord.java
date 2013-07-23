package net.aetherteam.aether.dungeons;

import java.io.Serializable;

public class TrackedCoord implements Serializable
{
    private int x;
    private int y;
    private int z;

    public TrackedCoord(int var1, int var2, int var3)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }
}
